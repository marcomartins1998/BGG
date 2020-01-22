package com.example.bgg.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.bgg.BGGApp.Companion.boardGamesDb
import com.example.bgg.model.Favourite

class FavouriteViewModel : ViewModel(){
    private val liveData : LiveData<List<Favourite>> = boardGamesDb.favouriteDao().getAll()

    val favourites : List<Favourite>
        get() = liveData.value ?: emptyList()

    fun observe(owner: LifecycleOwner, observer: (List<Favourite>) -> Unit) {
        liveData.observe(owner, Observer {
            observer(it)
        })
    }

}