package com.techchee.apps.newsapiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsFetchedListener, SearchViewOnChangeListener {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeRecyclerView()

        fetchNewsItems()
    }


    private fun initializeRecyclerView(){

        recyclerView = recycler_view
        val recyclerViewAdapter = RecyclerViewAdapter(null, this, this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
            )

            adapter = recyclerViewAdapter
        }
    }



    private fun fetchNewsItems( query : String = getString(R.string.default_search_text)){

        val n = NewsFetchingAsyncTask(query, this )
        n.execute()
    }

    override fun whenNewsFetchedSuccessfully(articles: List<Article>?) {

        val adapter = recyclerView.adapter as RecyclerViewAdapter
        adapter.refreshNewsItems(articles)
    }

    override fun whenNewsFetchedOnError(error: String?) {
        val t = Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT)
        t.setGravity(Gravity.TOP, 0, 500)
        t.show()

    }

    override fun searchViewOnQueryTextSubmit(text: String?) {

        val t = Toast.makeText(this, R.string.loading, Toast.LENGTH_SHORT)
        t.setGravity(Gravity.TOP, 0, 500)
        t.show()
        fetchNewsItems(text ?: "")

    }
}