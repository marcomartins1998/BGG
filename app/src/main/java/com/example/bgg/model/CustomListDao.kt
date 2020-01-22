package com.example.bgg.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomListDao {
    @Query("SELECT * FROM lists")
    fun getAll(): LiveData<List<CustomList>>

    @Query("SELECT * FROM lists WHERE name LIKE :name")
    fun findByName(name: String): LiveData<List<CustomList>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg list: CustomList)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(list: CustomList): Long

    @Delete
    fun delete(list: CustomList)
}