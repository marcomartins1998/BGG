package com.example.bgg.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.bgg.BGGApp
import com.example.bgg.model.CustomList

class CustomListsViewModel : ViewModel() {
    val TAG = "CustomListsViewModel"

    private val liveData: LiveData<List<CustomList>> = BGGApp.boardGamesDb.listDao().getAll()

    val customLists: List<CustomList>
        get() = liveData.value ?: emptyList()

    fun observe(owner: LifecycleOwner, observer: (List<CustomList>) -> Unit) {
        liveData.observe(owner, Observer {
            observer(it)
        })
    }
}