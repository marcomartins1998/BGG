package com.example.bgg.viewmodel

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.bgg.BGGApp
import com.example.bgg.LIST_ELEMENT_NUMBER
import com.example.bgg.api.BoardGamesWebApi
import com.example.bgg.model.Game

class GamesViewModel(private val boardGamesApi: BoardGamesWebApi) : ViewModel(), Parcelable {
    val TAG = "GamesViewModel"

    private val liveData : MutableLiveData<ArrayList<Game>> = MutableLiveData(arrayListOf())

    val games: ArrayList<Game>
        get() = liveData.value!!

    var urlid: String? = null
    var title: String? = null
    var skip: Int = 0
    var awaitChange: Boolean = false

    constructor(parcel: Parcel) : this(BGGApp.boardGamesApi) {
        val urlidaux = parcel.readString()!!
        //this.urlid = parcel.readString()!!
        this.title = parcel.readString()!!
        this.skip = parcel.readInt()
        getGames(urlidaux)
    }

    fun changePage(next: Boolean){
        if(!awaitChange) {
            this.awaitChange = true
            if (next) this.skip += LIST_ELEMENT_NUMBER
            else this.skip -= LIST_ELEMENT_NUMBER
            boardGamesApi.searchGames("$urlid&limit=30&skip=${this.skip}", {
                this.liveData.value = it
                this.awaitChange = false
            }, { Log.e(TAG, it.message ?: "Couldn't complete request!") })
        }
    }

    fun getGames(urlid: String) {
        if(urlid == this.urlid) return
        this.urlid = urlid
        boardGamesApi.searchGames("$urlid&limit=30&skip=${this.skip}", {
            this.liveData.value = it
        }, { Log.e(TAG, it.message ?: "Couldn't complete request!")})
    }

    fun observe(owner: LifecycleOwner, observer: (ArrayList<Game>) -> Unit) {
        liveData.observe(owner, Observer {
            observer(it)
        })
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(urlid)
        parcel.writeString(title)
        parcel.writeInt(skip)
    }

    override fun describeContents(): Int = 0


    companion object CREATOR : Parcelable.Creator<GamesViewModel> {
        override fun createFromParcel(parcel: Parcel): GamesViewModel {
            return GamesViewModel(parcel)
        }

        override fun newArray(size: Int): Array<GamesViewModel?> {
            return arrayOfNulls(size)
        }
    }

}