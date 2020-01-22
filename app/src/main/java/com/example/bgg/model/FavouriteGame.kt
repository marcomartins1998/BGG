package com.example.bgg.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["favourite", "game"], foreignKeys = [
    ForeignKey(
        entity = Favourite::class,
        parentColumns = ["name"],
        childColumns = ["favourite"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = Game::class,
        parentColumns = ["id"],
        childColumns = ["game"],
        onDelete = ForeignKey.CASCADE
    )]
)
class FavouriteGame (
    var favourite: String,
    var game: String
)