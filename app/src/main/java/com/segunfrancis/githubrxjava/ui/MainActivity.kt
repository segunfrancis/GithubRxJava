package com.segunfrancis.githubrxjava.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.segunfrancis.githubrxjava.data.GithubClient
import com.segunfrancis.githubrxjava.databinding.ActivityMainBinding
import com.segunfrancis.githubrxjava.model.GitHubRepo
import com.segunfrancis.githubrxjava.util.AppConstants.MAIN_TAG
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var subscription: Subscription? = null
    private val githubAdapter: GithubRepoAdapter by lazy { GithubRepoAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listViewRepos.apply {
            adapter = githubAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        binding.buttonSearch.setOnClickListener {
            val userNameET = binding.editTextUsername.text
            if (userNameET.isNullOrEmpty().not()) {
                getStarredRepos(userNameET.toString().trim())
            }
        }
    }

    private fun getStarredRepos(username: String) {
        loading()
        subscription = GithubClient().getInstance()?.getStarredRepos(username)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<List<GitHubRepo?>?> {
                override fun onCompleted() {
                    notLoading()
                    Log.d(MAIN_TAG, "In onCompleted()")
                }

                override fun onError(e: Throwable?) {
                    notLoading()
                    e?.printStackTrace()
                    Log.d(MAIN_TAG, "In onError()")
                    Toast.makeText(this@MainActivity, e?.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onNext(t: List<GitHubRepo?>?) {
                    notLoading()
                    githubAdapter.submitList(t)
                }
            })
    }

    private fun loading() {
        binding.progressIndicator.isVisible = true
        binding.buttonSearch.isEnabled = false
        hideSoftInput()
    }

    private fun notLoading() {
        binding.progressIndicator.isVisible = false
        binding.buttonSearch.isEnabled = true
    }

    private fun hideSoftInput() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onDestroy() {
        if (subscription != null && subscription!!.isUnsubscribed.not()) {
            subscription!!.unsubscribe()
        }
        super.onDestroy()
    }
}
