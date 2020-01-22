package com.example.bgg.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.example.bgg.model.Game

class GameViewModel() : ViewModel(), Parcelable {
    val TAG = "GameViewModel"

    lateinit var game: Game

    constructor(game: Game): this(){
        this.game = game
    }

    constructor(parcel: Parcel) : this() {
        game = parcel.readParcelable(Game::class.java.classLoader)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(game, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<GameViewModel> {
        override fun createFromParcel(parcel: Parcel): GameViewModel {
            return GameViewModel(parcel)
        }

        override fun newArray(size: Int): Array<GameViewModel?> {
            return arrayOfNulls(size)
        }
    }

}