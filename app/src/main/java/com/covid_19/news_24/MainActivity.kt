package com.covid_19.news_24

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity(), NewItemClicked {

    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler.layoutManager = LinearLayoutManager(this)

       fetchData()
        mAdapter = NewsListAdapter(this)
        recycler.adapter = mAdapter
    }


private fun fetchData() {
    //volly library
    val url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=26d0e73d5d7b44e8a4c905c433d6ace5"
    //making a request
    val jsonObjectRequest = object: JsonObjectRequest(
        Request.Method.GET,
        url,
        null,
        Response.Listener {
            val newsJsonArray = it.getJSONArray("articles")
            val newsArray = ArrayList<News>()
            for(i in 0 until newsJsonArray.length()) {
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
        Response.ErrorListener {
        }

    ) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["User-Agent"] = "Mozilla/5.0"
            return headers
        }
    }

    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
}
    override fun onItemClicked(item: News) {

       val  builder = CustomTabsIntent.Builder();
        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}