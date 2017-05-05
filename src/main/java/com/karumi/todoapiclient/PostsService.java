package com.karumi.todoapiclient;

import com.karumi.todoapiclient.dto.PostDto;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

import static com.karumi.todoapiclient.PostsApiClientConfig.POSTS_ENDPOINT;

interface PostsService {
    @GET(POSTS_ENDPOINT)
    Call<List<PostDto>> getAll();
}