package com.techchee.apps.newsapiapp

import android.os.AsyncTask
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


interface NewsFetchedListener {

    fun whenNewsFetchedSuccessfully ( articles : List<Article>?)

    fun whenNewsFetchedOnError ( error: String? )

}

class NewsFetchingAsyncTask (private val q : String? = null, private val newsFetchedListener: NewsFetchedListener? = null  ) : AsyncTask<String, String, String>(){



    @Throws(IOException::class)
    private fun sendGet(url: String): String {
        val obj = URL(url)
        val con = obj.openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        val responseCode = con.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
            val ins = BufferedReader(InputStreamReader(con.inputStream))
            val response = StringBuffer()

            var line : String?

            do {

                line = ins.readLine()

                if (line == null)
                    break

                response.append(line)


            } while (true)

            ins.close()
            return response.toString()
        } else {
            return ""
        }
    }

    override fun doInBackground(vararg p0: String?): String {

        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd",  Locale.getDefault())
        val dateAsString = formatter.format(date)

        val myurl = "https://newsapi.org/v2/everything?q=$q&from=$dateAsString&sortBy=publishedAt&apiKey=d94eca0209ff462abb2bc71315676332&language=en"

        val s = this.sendGet(myurl)

        return s
    }

    override fun onPostExecute(result: String?) {

        if ( result != null ){

            parseReturnedJsonData(result)
        }
    }


    private fun parseReturnedJsonData(s: String) {


        val p = Gson()
        val rt = p.fromJson(s, NewsResult::class.java)

        if ( rt.status == "ok" ){

            newsFetchedListener?.whenNewsFetchedSuccessfully(rt.articles)
        }
        else {

            newsFetchedListener?.whenNewsFetchedOnError("Error")
        }
    }

}