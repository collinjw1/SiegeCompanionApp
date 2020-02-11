package edu.rosehulman.collinjw.siegecompanionapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_user_stat_page.view.*
import org.json.JSONArray
import org.json.JSONObject
private const val NAME = "p_name"
private const val LEVEL = "p_level"
private const val MMR = "p_currentmmr"
private const val RANK = "p_currentrank"
private const val KD = "kd"

private const val baseUrl = "https://r6tab.com/api/search.php?platform=uplay&search="

class UserStatPage(val username: String? = "") : Fragment(), GetStatsTask.StatsConsumer {
    //private var listener: OnFragmentInteractionListener? = null
    lateinit var rootView: View
    lateinit var stats: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestUrl = baseUrl + username
        GetStatsTask(this).execute(requestUrl)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_stat_page, container, false)
        if (username == "") {
            rootView.user_not_found_tv.visibility = View.VISIBLE
        }
        return rootView
    }

    override fun onStatsLoaded(result: String?) {
        val obj = JSONObject(result)
        if (obj.get("totalresults") != 0) {
            val array = (JSONObject(result)?.get("results") as JSONArray)
            stats = array.getJSONObject(0)
            updateView()
        } else {
            rootView.user_not_found_tv.visibility = View.VISIBLE
        }
    }

    fun updateView() {
        rootView.player_name_tv.text = stats.get(NAME).toString() + "'s Stats"
        rootView.player_level_tv.text = "Level | " + stats.get(LEVEL).toString()
        rootView.player_mmr_tv.text = "Ranked MMR | " + stats.get(MMR).toString()
        rootView.player_kd_tv.text = "Ranked KD | " + ((stats.get(KD) as Int).toDouble() / 100).toString()
        handleRankIconAndString(stats.get(MMR) as Int)
    }

    fun handleRankIconAndString(mmr: Int) {
        if (mmr in 0 until 1400) {
            // copper 4
        } else if (mmr in 1400 until 1500) {
            // copper 3
        } else if (mmr in 1500 until 1600) {
            // copper 2
        } else if (mmr in 1600 until 1700) {
            // copper 1
        } else if (mmr in 1700 until 1800) {
            // bronze 4
        } else if (mmr in 1800 until 1900) {
            // bronze 3
        } else if (mmr in 1900 until 2000) {
            // bronze 2
        } else if (mmr in 2000 until 2100) {
            // bronze 1
        } else if (mmr in 2100 until 2200) {
            // silver 4
        } else if (mmr in 2200 until 2300) {
            // silver 3
        } else if (mmr in 2300 until 2400) {
            // silver 2
        } else if (mmr in 2400 until 2500) {
            // silver 1
        } else if (mmr in 2500 until 2700) {
            // gold 4
        } else if (mmr in 2700 until 2900) {
            // gold 3
        } else if (mmr in 2900 until 3100) {
            // gold 2
        } else if (mmr in 3100 until 3300) {
            // gold 1
        } else if (mmr in 3300 until 3700) {
            // plat 3
        } else if (mmr in 3700 until 4100) {
            // plat 2
        } else if (mmr in 4100 until 4500) {
            // plat 1
        } else if (mmr in 4500 until 5000) {
            // diamond
        } else {
            // champion
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
//    interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
//    }


}
