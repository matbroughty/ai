package com.broughty.ai.client;

import com.broughty.ai.ClientListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OaRestCalls {

    private static final Logger LOG = LoggerFactory.getLogger(OaRestCalls.class);

    private final RestClient restClient;

    public OaRestCalls(RestClient restClient) {
        this.restClient = restClient;
    }


    public ClientListResponse getClients(int pageNumber, int pageSize) {
        String url = String.format("system/clients?pageNumber=%d&pageSize=%d", pageNumber, pageSize);
        ClientListResponse clientListResponse = restClient.get().uri(url).accept(MediaType.APPLICATION_JSON)
                .retrieve().body(ClientListResponse.class);
        LOG.info("The response is {}", clientListResponse);
        return clientListResponse;
    }

}
