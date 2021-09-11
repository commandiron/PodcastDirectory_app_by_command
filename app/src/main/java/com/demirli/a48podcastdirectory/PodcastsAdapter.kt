package com.demirli.a48podcastdirectory

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PodcastsAdapter(var podcastList:List<Podcast>, var onPodcastItemClickListener: onPodcastListItemClickListener): RecyclerView.Adapter<PodcastsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.podcast_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = podcastList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.place.text = podcastList[position].place
        holder.episode.text = podcastList[position].episode

        holder.clickableLayout.setOnClickListener {
            onPodcastItemClickListener.onPodcastClick(podcastList[position])
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val place = view.findViewById<TextView>(R.id.place_tv)
        val episode = view.findViewById<TextView>(R.id.episode_tv)
        val clickableLayout = view.findViewById<LinearLayout>(R.id.clickable_linearLayout)
    }

    interface onPodcastListItemClickListener{
        fun onPodcastClick(podcast: Podcast)
    }
}