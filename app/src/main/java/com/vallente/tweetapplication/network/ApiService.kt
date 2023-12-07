package com.vallente.tweetapplication.network

import com.vallente.tweetapplication.models.Tweet
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/tweet/vallente")
    fun getTweetList(): Call<List<Tweet>>

    @GET("/tweet/vallente/{id}")
    fun getTweetById(@Path("id") id: String): Call<Tweet>

    @POST("/tweet/vallente")
    fun createTweet(@Body post: Tweet): Call<Tweet>

    @PUT("tweet/vallente/{id}")
    fun updateTweet(@Path("id") id: String, @Body updatedTweet: Tweet): Call<Tweet>

    @DELETE("tweet/vallente/{id}")
    fun deleteTweet(@Path("id") id: String): Call<Void>
}



