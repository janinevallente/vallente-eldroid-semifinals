package com.vallente.tweetapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.vallente.tweetapplication.databinding.ActivityCreateTweetBinding
import com.vallente.tweetapplication.models.Tweet
import com.vallente.tweetapplication.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTweetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTweetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTweetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.createTweetButton.setOnClickListener {
            val nameContent = binding.name.text.toString().trim()
            val descContent = binding.description.text.toString().trim()
            if (nameContent.isNotBlank() && descContent.isNotBlank()) {
                createTweet(nameContent,descContent)
            } else {
                showError("Tweet cannot be empty")
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

    private fun createTweet(nameContent: String, descriptionContent: String) {
        val tweet = Tweet(
            id = "",
            name = nameContent,
            description = descriptionContent
        )

        RetrofitClient.apiService.createTweet(tweet).enqueue(object : Callback<Tweet> {
            override fun onResponse(call: Call<Tweet>, response: Response<Tweet>) {
                if (response.isSuccessful) {
                    finish()
                } else {
                    val errorMessage = "Failed to create tweet. Status Code: ${response.code()}"
                    showError(errorMessage)
                }
            }

            override fun onFailure(call: Call<Tweet>, t: Throwable) {
                // Handle network failure
                showError("Network error. Please try again.")
            }
        })
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

}
