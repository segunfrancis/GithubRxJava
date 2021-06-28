package com.segunfrancis.githubrxjava.data

import android.util.Log
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.Retrofit
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.segunfrancis.githubrxjava.model.GitHubRepo
import com.segunfrancis.githubrxjava.util.AppConstants.GITHUB_BASE_URL
import com.segunfrancis.githubrxjava.util.AppConstants.NETWORK_TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import rx.Observable
import java.util.concurrent.TimeUnit

class GithubClient {
    private var instance: GithubClient? = null
    private var gitHubService: GithubService? = null

    init {
        retrofitBuilder()
    }

    private fun retrofitBuilder() {
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        val loggingInterceptor =
            HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        val client = OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AnalyticsInterceptor())
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .build()
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(GITHUB_BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        gitHubService = retrofit.create(GithubService::class.java)
    }

    fun getInstance(): GithubClient? {
        if (instance == null) {
            instance = GithubClient()
        }
        return instance
    }

    fun getStarredRepos(userName: String): Observable<List<GitHubRepo?>?>? {
        return gitHubService?.getStarredRepositories(userName)
    }
}
