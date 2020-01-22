package com.example.bgg.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
class Favourite (
    @PrimaryKey val name : String,
    val designer : String,
    val publisher: String,
    val mechanic: String,
    val category: String
)

