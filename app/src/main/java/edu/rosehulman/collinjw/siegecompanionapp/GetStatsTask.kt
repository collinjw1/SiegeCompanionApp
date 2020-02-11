package edu.rosehulman.collinjw.siegecompanionapp

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.lang.Exception
import java.net.URL

class GetStatsTask(private var consumer: StatsConsumer): AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String? {
        val url = URL(params[0])
        return try {
            val reader = BufferedReader(url.openStream().reader())
            reader.readText()
        } catch (e: Exception) {
            Log.e("HW_ERROR", "EXCEPTION: " + e.toString())
            null
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        consumer.onStatsLoaded(result)
    }

    interface StatsConsumer {
        fun onStatsLoaded(result: String?)
    }
}