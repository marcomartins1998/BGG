package com.example.bgg.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.bgg.BGGApp
import com.example.bgg.model.Game

class ListFavouriteGameViewModel(name: String, url: String) : ViewModel(){
    private val liveData : LiveData<List<Game>> = BGGApp.favouriteRepository.findByName(name,url)

    val games: List<Game>
        get() = liveData.value ?: emptyList()

    fun observe(owner: LifecycleOwner, observer: (List<Game>) -> Unit) {
        liveData.observe(owner, Observer {
            observer(it)
        })
    }

}