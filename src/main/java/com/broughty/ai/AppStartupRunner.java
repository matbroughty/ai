package com.broughty.ai;

import com.broughty.ai.client.OaRestCalls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private static final Logger LOG =
            LoggerFactory.getLogger(AppStartupRunner.class);

    OaRestCalls oaRestCalls;
    VectorStore vectorStore;

    public AppStartupRunner(OaRestCalls oaRestCalls, VectorStore vectorStore) {
        this.oaRestCalls = oaRestCalls;
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Application started with option names : {}",
                args.getOptionNames());
        LOG.info("Calling api to get clients");
        ClientListResponse clientListResponse = oaRestCalls.getClients(1, 100);

        LOG.info("Client list response: {}", clientListResponse);
        LOG.info("Total results: {}", clientListResponse.totalResults());
        LOG.info("Page number: {}", clientListResponse.pageNumber());
        LOG.info("Page size: {}", clientListResponse.pageSize());
        clientListResponse.result().forEach(client -> LOG.info("Client: {}", client));

//        String clients = clientListResponse.result().stream().map(Client::toString).collect(Collectors.joining());
//        vectorStore.add(List.of(new Document(clients)));

        clientListResponse.result().forEach(client -> {
            var document = new Document(client.toString());
            LOG.info("writing document to vector store: {} which is for client {}", document, client);
            vectorStore.add(List.of(document));
        });
    }
}