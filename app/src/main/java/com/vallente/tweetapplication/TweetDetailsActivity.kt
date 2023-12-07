package com.vallente.tweetapplication

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.vallente.tweetapplication.constants.Constants
import com.vallente.tweetapplication.databinding.ActivityTweetDetailsBinding
import com.vallente.tweetapplication.models.Tweet
import com.vallente.tweetapplication.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TweetDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTweetDetailsBinding
    private var tweet: Tweet? = null // Store the tweet data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTweetDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tweetId = intent.getStringExtra(Constants.PARAM_ID)
        if (tweetId != null) {
            getTweetDetails(tweetId)
        } else {
            showError()
        }

            binding.deleteButton.setOnClickListener {
            if (tweetId != null) {
                deleteTweet(tweetId)
            }
        }

        binding.updateButton.setOnClickListener {
            if (tweet != null) {
                navigateToUpdateTweet(tweet)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTweetDetails(tweetId: String) {
        RetrofitClient.apiService.getTweetById(tweetId).enqueue(object : Callback<Tweet> {
            override fun onResponse(call: Call<Tweet>, response: Response<Tweet>) {
                if (response.isSuccessful) {
                    val data: Tweet? = response.body()
                    data?.let {
                        tweet = it // Store the tweet data
                        binding.name.text = it.name // Display the name
                        binding.description.text = it.description
                        binding.progress.visibility = View.GONE
                    }
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<Tweet>, t: Throwable) {
                showError()
            }
        })
    }

    private fun deleteTweet(tweetId: String) {
        RetrofitClient.apiService.deleteTweet(tweetId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@TweetDetailsActivity, "Tweet deleted", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showError()
            }
        })
    }

    private fun showError() {
        Toast.makeText(this, "Failed to load data.", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToUpdateTweet(tweet: Tweet?) {
        val intent = Intent(this, UpdateTweetActivity::class.java)
        intent.putExtra(Constants.EXTRA_TWEET, tweet)
        startActivity(intent)
    }
}


