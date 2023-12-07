package com.vallente.tweetapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.vallente.tweetapplication.adapters.TweetAdapter
import com.vallente.tweetapplication.constants.Constants
import com.vallente.tweetapplication.databinding.ActivityListBinding
import com.vallente.tweetapplication.models.Tweet
import com.vallente.tweetapplication.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var tweetAdapter: TweetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.addTweetButton.setOnClickListener {
            startActivity(Intent(this, CreateTweetActivity::class.java))
        }

        binding.tweetList.layoutManager = LinearLayoutManager(this)
        tweetAdapter = TweetAdapter(this)
        binding.tweetList.adapter = tweetAdapter

        tweetAdapter.setOnItemClickListener(object : TweetAdapter.OnItemClickListener {
            override fun onItemClick(tweet: Tweet) {
                val intent = Intent(this@ListActivity, TweetDetailsActivity::class.java)
                intent.putExtra(Constants.PARAM_ID, tweet.id)
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getTweets()
    }

    private fun getTweets() {
        RetrofitClient.apiService.getTweetList().enqueue(object : Callback<List<Tweet>> {
            override fun onResponse(call: Call<List<Tweet>>, response: Response<List<Tweet>>) {
                if (response.isSuccessful) {
                    val data: List<Tweet>? = response.body()
                    data?.let {
                        tweetAdapter.submitList(it)
                        binding.progressBar.visibility = View.GONE
                    }
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<List<Tweet>>, t: Throwable) {
                showError()
            }
        })
    }

    private fun showError() {
        Toast.makeText(this, "Failed to load data.", Toast.LENGTH_SHORT).show()
    }
}
