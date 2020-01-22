package com.example.bgg.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Game::class, CustomList::class, CustomListGame::class, Favourite::class, FavouriteGame::class), version = 2)
abstract class BoardGamesDb : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun listDao(): CustomListDao
    abstract fun listGameDao(): CustomListGameDao
    abstract fun favouriteDao(): FavouriteDao
    abstract fun favouriteGameDao(): FavouriteGameDao
}