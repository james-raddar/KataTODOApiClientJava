package com.karumi.todoapiclient;

import org.junit.Before;
import org.junit.Test;

public class PostsApiClientTest extends MockWebServerTest{
    private PostsApiClient apiClient;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        String mockWebServerEndpoint = getBaseEndpoint();
        apiClient = new PostsApiClient(mockWebServerEndpoint);
    }


    @Test
    public void getAllPosts_sendsHeadersProperly() throws Exception {
        enqueueMockResponse();

        apiClient.getAllPost();

        assertRequestContainsHeader("Accept", "application/json");
    }
}