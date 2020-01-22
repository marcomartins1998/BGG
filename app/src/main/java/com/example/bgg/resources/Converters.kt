package com.example.bgg.resources

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun arrayToJson (value : Array<String>) : String{
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToArray (value : String) : Array<String>{
        val objects = Gson().fromJson(value,Array<String>::class.java) as Array<String>
        return objects
    }
}