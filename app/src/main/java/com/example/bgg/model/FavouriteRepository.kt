package com.example.bgg.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bgg.api.BoardGamesWebApi
import com.example.bgg.asynctask.DbActionsAsyncTask
import com.example.bgg.asynctask.FavouriteGamesAsyncTask


class FavouriteRepository (val boardGamesApi: BoardGamesWebApi, val boardGamesDb: BoardGamesDb){
    fun findByName(name: String, url: String): LiveData<List<Game>>{
        val res: MutableLiveData<List<Game>> = MutableLiveData()
        FavouriteGamesAsyncTask(boardGamesDb) {
            res.value = it
            checkFavouriteGames(res, name, url)
        }.execute(name)
        return res
    }

    fun checkFavouriteGames(res: MutableLiveData<List<Game>>, name: String, url: String){
        if(res.value != null && res.value!!.isNotEmpty())
            return

        boardGamesApi.searchGames(url, {
            val doTask = {
                boardGamesDb.gameDao().insertAll(*(it.toTypedArray()))
                boardGamesDb.favouriteGameDao().insertAll(*(it.map { FavouriteGame(name, it.id) }.toTypedArray()))
            }
            val onSuccess = { res.value = it }
            DbActionsAsyncTask(doTask, onSuccess).execute()
        }, { throw it })
    }
}
