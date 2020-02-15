package edu.rosehulman.collinjw.siegecompanionapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_user_stat_page.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
private const val LEVEL = "p_level"
private const val MMR = "NA_mmr"
private const val RANK = "NA_rank"
private const val KD = "kd"
private const val RANKED_STATS = "ranked"

private const val baseUrl = "https://r6tab.com/api/search.php?platform=uplay&search="

class UserStatPage(private val username: String? = "") : Fragment(), GetStatsTask.StatsConsumer {
    //private var listener: OnFragmentInteractionListener? = null
    private lateinit var rootView: View
    private lateinit var stats: JSONObject

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
        Log.d(Constants.STATS, "$result")
        if (result != null) {
            stats = JSONObject(result)
            displayElements(0)
            updateView()
        } else {
            displayElements(1)
        }
    }

    private fun displayElements(int: Int) {
        var errorVisibility: Int
        var infoVisibility: Int
        if (int == 0) {
            errorVisibility = View.GONE
            infoVisibility = View.VISIBLE
        } else {
            errorVisibility = View.VISIBLE
            infoVisibility = View.GONE
        }
        rootView.user_not_found_tv.visibility = errorVisibility
        rootView.player_name_tv.visibility = infoVisibility
        rootView.player_rank_iv.visibility = infoVisibility
        rootView.player_level_tv.visibility =infoVisibility
        rootView.player_rank_tv.visibility = infoVisibility
        rootView.player_mmr_tv.visibility = infoVisibility
        rootView.player_kd_tv.visibility = infoVisibility
    }

    private fun updateView() {
        val rankedStats = stats.getJSONObject(RANKED_STATS)
        rootView.player_name_tv.text = getString(R.string.player_stats_name, username)
        rootView.player_level_tv.text = getString(R.string.player_stats_level, stats.get(LEVEL).toString())
        rootView.player_mmr_tv.text = getString(R.string.player_stats_mmr, rankedStats.get(MMR).toString())
        rootView.player_kd_tv.text = getString(R.string.player_stats_kd, ((stats.get(KD) as Int).toDouble() / 100).toString())
        handleRankIconAndString(rankedStats.get(RANK) as Int)
    }

    private fun handleRankIconAndString(rank: Int) {
        val rankString: String
        val rankImage: Int
        when (rank) {
            PlayerRanks.UNRANKED.ordinal -> {
                rankString = "Unranked"
                rankImage = R.drawable.unranked_img
            }
            PlayerRanks.COPPER4.ordinal -> {
                rankString = "Copper 4"
                rankImage = R.drawable.copper4_img
            }
            PlayerRanks.COPPER3.ordinal -> {
                rankString = "Copper 3"
                rankImage = R.drawable.copper3_img
            }
            PlayerRanks.COPPER2.ordinal -> {
                rankString = "Copper 2"
                rankImage = R.drawable.copper2_img
            }
            PlayerRanks.COPPER1.ordinal -> {
                rankString = "Copper 1"
                rankImage = R.drawable.copper1_img
            }
            PlayerRanks.BRONZE4.ordinal -> {
                rankString = "Bronze 4"
                rankImage = R.drawable.bronze4_img
            }
            PlayerRanks.BRONZE3.ordinal -> {
                rankString = "Bronze 3"
                rankImage = R.drawable.bronze3_img
            }
            PlayerRanks.BRONZE2.ordinal -> {
                rankString = "Bronze 2"
                rankImage = R.drawable.bronze2_img
            }
            PlayerRanks.BRONZE1.ordinal -> {
                rankString = "Bronze 1"
                rankImage = R.drawable.bronze1_img
            }
            PlayerRanks.SILVER4.ordinal -> {
                rankString = "Silver 4"
                rankImage = R.drawable.silver4_img
            }
            PlayerRanks.SILVER3.ordinal -> {
                rankString = "Silver 3"
                rankImage = R.drawable.silver3_img
            }
            PlayerRanks.SILVER2.ordinal -> {
                rankString = "Silver 2"
                rankImage = R.drawable.silver2_img
            }
            PlayerRanks.SILVER1.ordinal -> {
                rankString = "Silver 1"
                rankImage = R.drawable.silver1_img
            }
            PlayerRanks.GOLD4.ordinal -> {
                rankString = "Gold 4"
                rankImage = R.drawable.gold4_img
            }
            PlayerRanks.GOLD3.ordinal -> {
                rankString = "Gold 3"
                rankImage = R.drawable.gold3_img
            }
            PlayerRanks.GOLD2.ordinal -> {
                rankString = "Gold 2"
                rankImage = R.drawable.gold2_img
            }
            PlayerRanks.GOLD1.ordinal -> {
                rankString = "Gold 1"
                rankImage = R.drawable.gold1_img
            }
            PlayerRanks.PLAT3.ordinal -> {
                rankString = "Plat 3"
                rankImage = R.drawable.plat3_img
            }
            PlayerRanks.PLAT2.ordinal -> {
                rankString = "Plat 2"
                rankImage = R.drawable.plat2_img
            }
            PlayerRanks.PLAT1.ordinal -> {
                rankString = "Plat 1"
                rankImage = R.drawable.plat1_img
            }
            PlayerRanks.DIAMOND.ordinal -> {
                rankString = "Diamond"
                rankImage = R.drawable.diamond_img
            }
            PlayerRanks.CHAMP.ordinal -> {
                rankString = "Champion"
                rankImage = R.drawable.champ_img
            }
            else -> {
                rankString = "Diamond"
                rankImage = R.drawable.diamond_img
            }
        }
        rootView.player_rank_tv.text = getString(R.string.player_stats_rank, rankString)
        rootView.player_rank_iv.setImageResource(rankImage)
    }
}
