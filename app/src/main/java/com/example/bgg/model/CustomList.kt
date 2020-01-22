package com.example.bgg.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
class CustomList (
    @PrimaryKey val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) { parcel.writeString(name) }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomList> {
        override fun createFromParcel(parcel: Parcel): CustomList {
            return CustomList(parcel)
        }

        override fun newArray(size: Int): Array<CustomList?> {
            return arrayOfNulls(size)
        }
    }

}