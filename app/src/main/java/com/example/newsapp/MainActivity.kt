package com.example.newsapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import com.android.volley.AuthFailureError
import com.android.volley.Request.*
import com.android.volley.toolbox.JsonObjectRequest

import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager as LinearLayoutManager1
import com.android.volley.Response as Responsclass


     class MainActivity : AppCompatActivity(),NewsItemClicked {
         private lateinit var mAdapter: NewsListAdapter

    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager1(this)
         fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter

    }
    private fun fetchData() {
        val url = "https://newsapi.org/v2/everything?domains=wsj.com&apiKey=59ff72c03b134a339f24a34db7c55d22"
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url,
            null,
            Responsclass.Listener {
             val newsJsonArray = it.getJSONArray("articles")
             val newsArray = ArrayList<News>()
             for (i in 0 until newsJsonArray.length()){
                 val newsJsonObject = newsJsonArray.getJSONObject(i)
                 val news = News(
                     newsJsonObject.getString("title"),
                     newsJsonObject.getString("author"),
                     newsJsonObject.getString("url"),
                     newsJsonObject.getString("urlToImage")

                 )
                 newsArray.add(news)
             }

               mAdapter.updateNews(newsArray)

            },



            Responsclass.ErrorListener {

            }
        )

        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

         override fun onItemClicked(item: News) {

             val builder = CustomTabsIntent.Builder()
             val customTabsIntent = builder.build()
             customTabsIntent.launchUrl(this, Uri.parse(item.url))

         }



     }


