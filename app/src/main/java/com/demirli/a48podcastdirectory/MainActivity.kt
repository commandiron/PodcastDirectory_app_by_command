package com.demirli.a48podcastdirectory

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(), PodcastsAdapter.onPodcastListItemClickListener {

    private lateinit var podcastAdapter: PodcastsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSpinnerAdapter()

        setRecyclerViewAdapter(arrayListOf(Podcast("1", "Episode1", "Playlink1")))

        get_btn.setOnClickListener {
            if(spinner.selectedItem.toString() == "Select Podcaster"){
                Toast.makeText(this, "Please select podcaster", Toast.LENGTH_SHORT).show()
            }else if(spinner.selectedItem.toString() == "JavaScript Jabber"){
                AsyncTaskForGetHtml().execute("https://www.podbean.com/podcast-detail/d4un8-57595/JavaScript-Jabber-Podcast")
            }else if(spinner.selectedItem.toString() == "Techpoint Charlie"){
                AsyncTaskForGetHtml().execute("https://www.podbean.com/podcast-detail/k76vd-8adc7/Techpoint-Charlie-Podcast")
            }
        }
    }

    private fun setSpinnerAdapter(){
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, listOf("Select Podcaster", "JavaScript Jabber" , "Techpoint Charlie"))
        spinner.adapter = adapter
    }

    private fun setRecyclerViewAdapter(podcastList: List<Podcast>){
        recyclerView.layoutManager = LinearLayoutManager(this)
        podcastAdapter = PodcastsAdapter(podcastList, this)
        recyclerView.adapter = podcastAdapter
    }

    inner class AsyncTaskForGetHtml: AsyncTask<String, Void, Document>() {
        override fun doInBackground(vararg params: String?): Document {
            var url: String
            url = params[0]!!
            val doc = Jsoup.connect(url).get()
            return doc
        }

        override fun onPostExecute(result: Document?) {
            super.onPostExecute(result)

            val podcastList = arrayListOf<Podcast>()

            for(i in 1 until 10){
                val episode = result!!.select("#yw0 > table > tbody > tr:nth-child($i) > td:nth-child(2)").text()
                val playlink = result!!.select("#yw0 > table > tbody > tr:nth-child($i) > td:nth-child(3) > a").attr("href")
                val podcast = Podcast(i.toString(),episode,playlink)
                podcastList.add(podcast)
            }

            setRecyclerViewAdapter(podcastList)
        }
    }

    override fun onPodcastClick(podcast: Podcast) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(podcast.playlink)
        startActivity(intent)
    }
}
