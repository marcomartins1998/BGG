package com.example.bgg.api

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bgg.asynctask.GamesAsyncTask
import com.example.bgg.model.Game

class BoardGamesWebApi(ctx: Context){
    val TAG = "BoardGamesWebApi"

    val queue = Volley.newRequestQueue(ctx, HurlStack())

    fun searchGames(url: String, onSuccess: (ArrayList<Game>) -> Unit, onError: (VolleyError) -> Unit) {
        Log.d(TAG, "Request for games made!")
        val asyncTask = GamesAsyncTask(onSuccess)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response -> asyncTask.execute(response) },
            Response.ErrorListener { err -> onError(err) })
        queue.add(stringRequest)
    }
}





