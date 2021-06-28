package com.segunfrancis.githubrxjava.data

import com.segunfrancis.githubrxjava.model.GitHubRepo
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface GithubService {
    @GET("users/{user}/starred")
    fun getStarredRepositories(@Path("user") userName: String): Observable<List<GitHubRepo?>?>
}
