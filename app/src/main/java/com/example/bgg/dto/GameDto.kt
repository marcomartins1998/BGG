package com.example.bgg.dto

import com.example.bgg.model.Game

class GameDto(
    val id: String,
    val name: String,
    val image_url: String,
    val thumb_url: String,
    val min_players: Int? = 0,
    val max_players: Int? = 0,
    val min_age: Int? = 0,
    val description_preview: String,
    val primary_publisher: String?,
    val designers: Array<String>,
    val average_user_rating: Double,
    val rules_url: String?,
    val url: String
){
    fun toGame() = Game(
        id,
        name,
        image_url,
        thumb_url,
        min_players?.toString() ?: "No Data",
        max_players?.toString() ?: "No Data",
        min_age?.toString() ?: "No Data",
        description_preview,
        primary_publisher ?: "No Data",
        designers,
        average_user_rating,
        rules_url ?: "No Data",
        url
    )
}