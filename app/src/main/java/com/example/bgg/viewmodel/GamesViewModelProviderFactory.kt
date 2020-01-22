package com.example.bgg.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bgg.*

class GamesViewModelProviderFactory(val savedInstanceState: Bundle?, val activity: GameListActivity) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var gamesModel: GamesViewModel? = null
        if(savedInstanceState != null){ gamesModel = savedInstanceState.getParcelable(GAME_LIST_VIEW_STATE) }
        if(gamesModel != null) {
            Log.v(TAG, "**** RESTORED GamesViewModel from Bundle!!!")
            gamesModel.getGames(activity.intent.getStringExtra(GET_URL)!!)
            return gamesModel as T
        } else if(modelClass == GamesViewModel::class.java) {
            Log.v(TAG, "**** CREATED GamesViewModel from the scratch!!!")
            gamesModel = GamesViewModel(BGGApp.boardGamesApi)
            gamesModel.getGames(activity.intent.getStringExtra(GET_URL)!!)
            gamesModel.title = activity.intent.getStringExtra(TITLE)
            return gamesModel as T
        } else throw IllegalArgumentException("There is no ViewModel for class $modelClass")
    }
}

