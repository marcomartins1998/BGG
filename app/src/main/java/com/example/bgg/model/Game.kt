package com.example.bgg.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.bgg.resources.Converters

@Entity(tableName = "games")
@TypeConverters(Converters::class)
class Game(
    @PrimaryKey val id: String,
    val name: String,
    val image_url: String,
    val thumb_url: String,
    val min_players: String,
    val max_players: String,
    val min_age: String,
    val description_preview: String,
    val primary_publisher: String,
    val designers: Array<String>,
    val average_user_rating: Double,
    val rules_url: String,
    val url: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArray()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(image_url)
        parcel.writeString(thumb_url)
        parcel.writeString(min_players)
        parcel.writeString(max_players)
        parcel.writeString(min_age)
        parcel.writeString(description_preview)
        parcel.writeString(primary_publisher)
        parcel.writeStringArray(designers)
        parcel.writeDouble(average_user_rating)
        parcel.writeString(rules_url)
        parcel.writeString(url)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }

}