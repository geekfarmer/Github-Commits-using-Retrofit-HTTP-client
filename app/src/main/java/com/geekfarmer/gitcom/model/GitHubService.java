package com.geekfarmer.gitcom.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by geekfarmer
 */
public interface GitHubService {
    
    /**
     * https://developer.github.com/v3/users/#get-a-single-user
     */
    @GET("users/{username}")
    Call<User> getUser(@Path("username") String user);
    
    /**
     * https://developer.github.com/v3/repos/#list-user-repositories
     */
    @GET("users/{user}/repos")
    Call<List<Repos>> listRepos(@Path("user") String user);

    /**
     * https://developer.github.com/v3/repos/commits/#list-commits-on-a-repository
     */
    @GET("repos/{owner}/{repos}/commits")
    Call<List<Commits>> listCommit(@Path("owner") String owner, @Path("repos") String repos);
}