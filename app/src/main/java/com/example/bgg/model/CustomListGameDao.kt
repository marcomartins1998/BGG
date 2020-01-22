package com.example.bgg.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomListGameDao {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query(
        """SELECT * FROM games INNER JOIN CustomListGame ON
        games.id = CustomListGame.game WHERE
        CustomListGame.list = :listname ORDER BY average_user_rating DESC """
    )
    fun getGamesWithListName(listname: String): LiveData<List<Game>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg customListGame: CustomListGame)

    @Delete
    fun delete(list: CustomListGame)
}