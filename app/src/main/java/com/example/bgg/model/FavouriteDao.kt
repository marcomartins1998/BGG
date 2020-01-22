package com.example.bgg.model


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourites")
    fun getAll(): LiveData<List<Favourite>>

    @Query("SELECT * FROM favourites")
    fun getAllSync(): List<Favourite>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavourite(favourite: Favourite): Long

    @Delete
    fun delete(favourite: Favourite)
}