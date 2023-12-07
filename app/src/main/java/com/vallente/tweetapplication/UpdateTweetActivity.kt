package com.vallente.tweetapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.vallente.tweetapplication.constants.Constants
import com.vallente.tweetapplication.databinding.ActivityUpdateTweetBinding
import com.vallente.tweetapplication.models.Tweet
import com.vallente.tweetapplication.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateTweetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateTweetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTweetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @Suppress("DEPRECATION")
        val tweet = intent.getSerializableExtra(Constants.EXTRA_TWEET) as? Tweet

        if (tweet != null) {
            bindData(tweet)

            binding.updateTweetButton.setOnClickListener {
                val updatedName = binding.updateName.text.toString().trim()
                val updatedDescription = binding.updateDescription.text.toString().trim()

                if (updatedName.isNotEmpty() && updatedDescription.isNotEmpty()) {
                    val updatedTweet = Tweet(tweet.id, updatedName, updatedDescription)
                    updateTweet(tweet.id, updatedTweet)
                } else {
                    Toast.makeText(this@UpdateTweetActivity,"Empty Fields.",Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            showError()
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



    private fun bindData(tweet: Tweet) {
        binding.updateName.setText(tweet.name)
        binding.updateDescription.setText(tweet.description)
    }

    private fun updateTweet(tweetId: String, updatedTweet: Tweet) {
        RetrofitClient.apiService.updateTweet(tweetId, updatedTweet).enqueue(object : Callback<Tweet> {
            override fun onResponse(call: Call<Tweet>, response: Response<Tweet>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateTweetActivity, "Tweet updated", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@UpdateTweetActivity, ListActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<Tweet>, t: Throwable) {
                showError()
            }
        })
    }

    private fun showError() {
        Toast.makeText(this, "Failed to load data.", Toast.LENGTH_SHORT).show()
    }
}

