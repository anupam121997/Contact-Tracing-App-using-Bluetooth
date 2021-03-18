package com.example.projectinterface;

import kotlinx.coroutines.Dispatchers

import android.widget.ListView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.example.projectinterface.CovidUpdate.stateListAdapter
import com.example.projectinterface.Client
import com.example.projectinterface.CovidUpdate.getTimeAgo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

object FetchResults {
    @JvmStatic
    fun fetchResults(swipeToRefresh : SwipeRefreshLayout, lastUpdatedTv : TextView, list : ListView,  confirmedTv : TextView, activeTv : TextView, recoveredTv : TextView, deceasedTv : TextView) {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.clone().execute() }
            if (response.isSuccessful) {
                swipeToRefresh.isRefreshing = false
                val data = Gson().fromJson(response.body?.string(), Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedData(data.statewise[0], lastUpdatedTv, confirmedTv, activeTv, recoveredTv, deceasedTv)
                    bindStateWiseData(data.statewise.subList(0, data.statewise.size), list)
                }
            }
        }
    }

    private fun bindStateWiseData(subList: List<StatewiseItem>, list : ListView) {
        stateListAdapter = StateListAdapter(subList)
        list.adapter = stateListAdapter
    }

    private fun bindCombinedData(data: StatewiseItem, lastUpdatedTv : TextView, confirmedTv : TextView, activeTv : TextView, recoveredTv : TextView, deceasedTv : TextView) {
        val lastUpdatedTime = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastUpdatedTv.text = "Last Updated\n ${getTimeAgo(
            simpleDateFormat.parse(lastUpdatedTime)
        )}"

        confirmedTv.text = data.confirmed
        activeTv.text = data.active
        recoveredTv.text = data.recovered
        deceasedTv.text = data.deaths

    }
}