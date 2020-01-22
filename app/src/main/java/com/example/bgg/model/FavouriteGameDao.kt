package com.example.bgg.model

import androidx.room.*

@Dao
interface FavouriteGameDao {

    @Query(
        """SELECT * FROM games INNER JOIN FavouriteGame ON
        games.id = FavouriteGame.game WHERE
        FavouriteGame.favourite = :name"""
    )
    fun getGamesWithFavouriteName(name: String): List<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg favouriteGame: FavouriteGame)

    @Delete
    fun delete(favourite: FavouriteGame)
}