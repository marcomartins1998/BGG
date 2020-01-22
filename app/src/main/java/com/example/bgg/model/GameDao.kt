package com.example.bgg.model

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAll(): LiveData<List<Game>>

    @Query("SELECT * FROM games WHERE id LIKE :id")
    fun findByName(id: String): LiveData<List<Game>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg games: Game)

    @Delete
    fun delete(game: Game)
}