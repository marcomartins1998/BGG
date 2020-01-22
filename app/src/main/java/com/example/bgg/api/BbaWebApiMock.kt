package com.example.bgg.api

import com.example.bgg.dto.GameDto
import com.example.bgg.resources.arrayOfGames

class BbaWebApiMock {
    fun filterName(arrayGames: ArrayList<GameDto>, namegame: String) : ArrayList<GameDto> {
        return arrayGames.filter { it.name == namegame } as ArrayList<GameDto>
    }

    fun filterCompany(arrayGames: ArrayList<GameDto>, nameCompany: String) : ArrayList<GameDto> {
        return arrayGames.filter { it.primary_publisher == nameCompany } as ArrayList<GameDto>
    }

    val games: ArrayList<GameDto> = arrayOfGames()
}