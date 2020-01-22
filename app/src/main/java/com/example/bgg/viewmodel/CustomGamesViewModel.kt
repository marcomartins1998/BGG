package com.example.bgg.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.bgg.BGGApp
import com.example.bgg.model.Game

class CustomGamesViewModel(val listname: String) : ViewModel(){

    private val liveData : LiveData<List<Game>> = BGGApp.boardGamesDb.listGameDao().getGamesWithListName(listname)

    val games: ArrayList<Game>
        get() = if(liveData.value != null) ArrayList(liveData.value!!) else ArrayList(emptyList<Game>())

    fun observe(owner: LifecycleOwner, observer: (List<Game>) -> Unit) {
        liveData.observe(owner, Observer {
            observer(it)
        })
    }
}


