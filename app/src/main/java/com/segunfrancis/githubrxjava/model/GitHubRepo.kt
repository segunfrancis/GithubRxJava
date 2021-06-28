package com.segunfrancis.githubrxjava.model

data class GitHubRepo(
    val id: Int,
    val name: String,
    val htmlUrl: String,
    val description: String?,
    val language: String,
    val stargazersCount: Int
)
