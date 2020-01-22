package com.example.bgg.asynctask

import android.os.AsyncTask
import com.example.bgg.model.BoardGamesDb
import com.example.bgg.model.Game

class FavouriteGamesAsyncTask(val boardGamesDb: BoardGamesDb, val onSucess: (List<Game>?) -> Unit) : AsyncTask<String, Unit, List<Game>>() {
    override fun doInBackground(vararg params: String?): List<Game>? {
        return boardGamesDb.favouriteGameDao().getGamesWithFavouriteName(params[0]!!)
    }

    override fun onPostExecute(result: List<Game>?) {
        super.onPostExecute(result)
        onSucess(result)
    }
}