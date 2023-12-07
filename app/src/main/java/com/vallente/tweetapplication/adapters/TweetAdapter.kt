package com.vallente.tweetapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vallente.tweetapplication.databinding.ItemTweetBinding
import com.vallente.tweetapplication.models.Tweet

class TweetAdapter(private val context: Context) : RecyclerView.Adapter<TweetAdapter.TweetViewHolder>() {

    private val tweetList: MutableList<Tweet> = mutableListOf()
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(tweet: Tweet)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class TweetViewHolder(private val binding: ItemTweetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tweet: Tweet) {
            binding.name.text = tweet.name
            binding.description.text = tweet.description
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(tweet)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val binding = ItemTweetBinding.inflate(LayoutInflater.from(context), parent, false)
        return TweetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        val tweet = tweetList[position]
        holder.bind(tweet)
    }

    override fun getItemCount(): Int {
        return tweetList.size
    }

    fun submitList(tweets: List<Tweet>) {
        tweetList.clear()
        tweetList.addAll(tweets)
        notifyDataSetChanged()
    }
}
