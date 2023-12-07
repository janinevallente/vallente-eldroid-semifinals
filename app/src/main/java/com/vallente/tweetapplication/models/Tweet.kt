package com.vallente.tweetapplication.models

import java.io.Serializable

data class Tweet(
    var id: String,
    var name: String,
    var description: String
) : Serializable
