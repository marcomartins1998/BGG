package com.example.bgg

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.adapter.ListFavouriteGameAdapter
import com.example.bgg.adapter.NAME
import com.example.bgg.viewmodel.ListFavouriteGameViewModel
import com.example.bgg.viewmodel.ListFavouriteGameViewModelProviderFactory

const val FAVOURITE_NAME = "FAVOURITE_NAME_VIEW_STATE"
const val FAVOURITE_URL = "FAVOURITE_URL_VIEW_STATE"

class ListFavouriteGamesActivity : AppCompatActivity(){
    val TAG = "ListFavouriteGamesA"

    val tvFavouriteName: TextView by lazy { findViewById<TextView>(R.id.tvFavouriteName) }
    val rvFavouriteGamesList: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rvFavouriteGamesList) }
    val name: String? by lazy { intent.getStringExtra(NAME) }
    val url: String? by lazy { intent.getStringExtra(GET_URL) }

    val listFavouriteGameAdapter : ListFavouriteGameAdapter by lazy { ListFavouriteGameAdapter(favouriteGameModel) }

    lateinit var favouriteGameModel : ListFavouriteGameViewModel

    fun initGamesViewModel(savedInstanceState: Bundle?): ListFavouriteGameViewModel {
        val factory = ListFavouriteGameViewModelProviderFactory(savedInstanceState, name!!,url!!)
        return ViewModelProviders.of(this, factory)[ListFavouriteGameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_favourite_games)
        this.lifecycle.addObserver(LifecycleLogger(componentName.className))

        tvFavouriteName.text = name
        favouriteGameModel = initGamesViewModel(savedInstanceState)
        favouriteGameModel.observe(this){ listFavouriteGameAdapter.notifyDataSetChanged() }
        rvFavouriteGamesList.layoutManager = LinearLayoutManager(this)
        rvFavouriteGamesList.adapter = listFavouriteGameAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(isChangingConfigurations) return
        outState.putString(FAVOURITE_NAME,name)
        outState.putString(FAVOURITE_URL,url)
    }
}