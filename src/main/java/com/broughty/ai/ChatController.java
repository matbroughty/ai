package com.broughty.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Controller
@ResponseBody
public class ChatController {


    private final ChatClient chatClient;

    private final Map<String, PromptChatMemoryAdvisor> advisorMap = new ConcurrentHashMap<>();

    private final QuestionAnswerAdvisor questionAndAnswerAdvisor;

    ChatController(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        for (int i = 0; i < 10; i++) {
            var document = new Document("id: %s customer name: %s address: %s".formatted(i, "Customer" + i, "Address" + i));
            vectorStore.add(List.of(document));
            document = new Document("id: %s supplier name: %s address: %s".formatted(i, "Supplier" + i, "Address" + i));
            vectorStore.add(List.of(document));
        }
        this.questionAndAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore).searchRequest(SearchRequest.builder().build())
                .build();
    }

    @PostMapping("/{user}/inquire")
    String inquire(@PathVariable String user, @RequestParam String question) {

        var advisor = this.advisorMap.computeIfAbsent(user, eh ->
                PromptChatMemoryAdvisor.builder(MessageWindowChatMemory
                                .builder()
                                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                                .build())
                        .build());

        return chatClient
                .prompt()
                .system("You are a helpful assistant who answers questions.  The user is %s".formatted(user)
                        + " and the questions are centred around the receivables finance domain.  Clients (SMEs) have Customers (debtors) and Suppliers (creditors).")
                .user(question)
                .advisors(advisor, questionAndAnswerAdvisor)
                .call()
                .content();
    }

}
