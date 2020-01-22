package com.example.bgg.asynctask

import android.os.AsyncTask
import com.example.bgg.dto.GameDto
import com.example.bgg.dto.GamesMatchesDto
import com.example.bgg.model.Game
import com.google.gson.GsonBuilder

class GamesAsyncTask(val onSuccess: (ArrayList<Game>) -> Unit) : AsyncTask<String, Int, ArrayList<Game>>() {
    override fun doInBackground(vararg response: String?): ArrayList<Game> {
        val gson = GsonBuilder().serializeNulls().create()
        return ArrayList(gson.fromJson<GamesMatchesDto>(response[0], GamesMatchesDto::class.java).games.map(GameDto::toGame))
    }

    override fun onPostExecute(result: ArrayList<Game>) = onSuccess(result)
}