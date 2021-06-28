package com.segunfrancis.githubrxjava.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.segunfrancis.githubrxjava.model.GitHubRepo
import androidx.recyclerview.widget.RecyclerView
import com.segunfrancis.githubrxjava.R
import com.segunfrancis.githubrxjava.databinding.ItemGithubRepoBinding

class GithubRepoAdapter : ListAdapter<GitHubRepo, GithubRepoAdapter.GitHubRepoViewHolder>(GithubRepoDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubRepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_github_repo, parent, false)
        return GitHubRepoViewHolder(ItemGithubRepoBinding.bind(view))
    }

    override fun onBindViewHolder(holder: GitHubRepoViewHolder, position: Int) {
        holder.setGitHubRepo(getItem(position))
    }

    class GithubRepoDiffUtil : DiffUtil.ItemCallback<GitHubRepo?>() {
        override fun areItemsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo): Boolean {
            return oldItem == newItem
        }
    }

    class GitHubRepoViewHolder(private val binding: ItemGithubRepoBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setGitHubRepo(gitHubRepo: GitHubRepo?) = with(binding) {
            textRepoName.text = gitHubRepo?.name
            textRepoDescription.text = gitHubRepo?.description
            textLanguage.text = "Language: ".plus(gitHubRepo?.language)
            textStars.text = "Stars: ".plus(gitHubRepo?.stargazersCount)
        }
    }
}
