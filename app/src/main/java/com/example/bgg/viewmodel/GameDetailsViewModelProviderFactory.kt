package com.example.bgg.viewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bgg.GAME
import com.example.bgg.GAME_VIEW_STATE
import com.example.bgg.TAG
import com.example.bgg.model.Game

class GameDetailsViewModelProviderFactory(val savedInstanceState: Bundle?, val intent: Intent) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var gameModel: GameViewModel? = null
        if(savedInstanceState != null){ gameModel = savedInstanceState.getParcelable(GAME_VIEW_STATE) }
        if(gameModel != null) {
            Log.v(TAG, "**** RESTORED GameViewModel from Bundle!!!")
            return gameModel as T
        } else if(modelClass == GameViewModel::class.java) {
            Log.v(TAG, "**** CREATED GameViewModel from the scratch!!!")
            gameModel = GameViewModel(intent.getParcelableExtra<Game>(GAME)!!)
            return gameModel as T
        } else throw IllegalArgumentException("There is no ViewModel for class $modelClass")
    }
}