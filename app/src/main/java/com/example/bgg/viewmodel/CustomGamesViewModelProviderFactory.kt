package com.example.bgg.viewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bgg.TAG
import com.example.bgg.adapter.CUSTOM_LIST_NAME

class CustomGamesViewModelProviderFactory(val savedInstanceState: Bundle?, val intent: Intent) : ViewModelProvider.Factory {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        var listname: String? = null

        if(savedInstanceState != null){
            listname = savedInstanceState.getString(CUSTOM_LIST_NAME)
        }
        if (listname != null){
            Log.v(TAG, "**** RESTORED CustomList name from Bundle!!!")
            return CustomGamesViewModel(listname) as T
        }
        if(modelClass == CustomGamesViewModel::class.java) {
            Log.v(TAG, "**** CREATED GamesViewModel from the scratch!!!")
            return CustomGamesViewModel(intent.getStringExtra(CUSTOM_LIST_NAME)!!) as T
        } else throw IllegalArgumentException("There is no ViewModel for class $modelClass")
    }
}

