package com.example.bgg.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["list", "game"], foreignKeys = [
        ForeignKey(
            entity = CustomList::class,
            parentColumns = ["name"],
            childColumns = ["list"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Game::class,
            parentColumns = ["id"],
            childColumns = ["game"]
        )]
)
class CustomListGame (
    var list: String,
    var game: String
)