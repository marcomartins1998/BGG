package com.example.bgg.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bgg.FAVOURITE_NAME
import com.example.bgg.FAVOURITE_URL

class ListFavouriteGameViewModelProviderFactory (val savedInstanceState: Bundle?, val nameF : String, val urlF : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var listName: String? = null
        var url: String? = null

        if(savedInstanceState != null ) {
            listName = savedInstanceState.getString(FAVOURITE_NAME)
            url = savedInstanceState.getString(FAVOURITE_URL)
        }
        if(listName != null && url != null){
            return ListFavouriteGameViewModel(listName,url) as T
        } else if (modelClass == ListFavouriteGameViewModel::class.java){
            return ListFavouriteGameViewModel(nameF, urlF) as T
        } else throw IllegalArgumentException("There is no ViewModel for class $modelClass")
    }
}