package edu.rosehulman.collinjw.siegecompanionapp

import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.lang.Exception
import java.net.URL

class GetStatsTask(private var consumer: StatsConsumer) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String? {
        var url = URL(params[0])
        try {
            var reader = BufferedReader(url.openStream().reader())
            val obj = JSONObject(reader.readText())
            val array = (obj.get("results") as JSONArray)
            val stats = array.getJSONObject(0)
            val pid = stats.get("p_id")
            url = URL("https://r6tab.com/api/player.php?p_id=$pid")
            reader = BufferedReader(url.openStream().reader())
            return reader.readText()
        } catch (e: Exception) {
            Log.e("HW_ERROR", "EXCEPTION: " + e.toString())
            return null
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