package com.example.bgg.resources

import com.example.bgg.model.Favourite

const val BASE_URL = "https://www.boardgameatlas.com/api/search?client_id=p2q1KFQiUy"

fun formatFavouriteUrl(favourite: Favourite): String{

    val url = StringBuilder()
    url.append("https://www.boardgameatlas.com/api/search?client_id=p2q1KFQiUy")

    val category = categoriesMap().get(favourite.category)
    val mechanic = mechanicsMap().get(favourite.mechanic)
    val publisher = favourite.publisher.replace(" ","%20")
    val designer = favourite.designer.replace(" ","%20")

    if(category != null) url.append("&categories=$category")
    if(mechanic != null) url.append("&mechanics=$mechanic")
    url.append("&publisher=$publisher")
    url.append("&designer=$designer")

    return url.toString()
}