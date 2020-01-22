package com.example.bgg

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.adapter.GamesAdapter
import com.example.bgg.viewmodel.GamesViewModel
import com.example.bgg.viewmodel.GamesViewModelProviderFactory

const val GAME_LIST_VIEW_STATE = "GAME_LIST_VIEW_STATE"
const val LIST_ELEMENT_NUMBER = 30

class GameListActivity : AppCompatActivity() {
    val TAG = "GameListActivity"

    val tvTitle: TextView by lazy { findViewById<TextView>(R.id.TextView) }
    val rvGames: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rvCustomLists) }
    val tvPageNumber: TextView by lazy { findViewById<TextView>(R.id.tvPageNumber) }
    val bPrevious: Button by lazy { findViewById<Button>(R.id.bPrevious) }
    val bNext: Button by lazy { findViewById<Button>(R.id.bNext) }

    val gamesAdapter: GamesAdapter by lazy { GamesAdapter(gamesModel) }

    lateinit var gamesModel: GamesViewModel

    fun initGamesViewModel(savedInstanceState: Bundle?): GamesViewModel{
        val factory = GamesViewModelProviderFactory(savedInstanceState, this)
        return ViewModelProviders.of(this, factory)[GamesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_games)
        this.lifecycle.addObserver(LifecycleLogger(componentName.className))

        gamesModel = initGamesViewModel(savedInstanceState)
        gamesModel.observe(this){
            gamesAdapter.notifyDataSetChanged()
            tvPageNumber.text = ((gamesModel.skip / LIST_ELEMENT_NUMBER) + 1).toString()
            if(gamesModel.skip == 0){ bPrevious.isClickable = false; bPrevious.alpha = 0.5F } else{ bPrevious.isClickable = true; bPrevious.alpha = 1F }
            if(gamesModel.games.size < LIST_ELEMENT_NUMBER){ bNext.isClickable = false; bNext.alpha = 0.5F } else{ bNext.isClickable = true; bNext.alpha = 1F }
        }
        rvGames.layoutManager = LinearLayoutManager(this)
        rvGames.adapter = gamesAdapter
        tvTitle.text = gamesModel.title
        setupPageButtons()
    }

    fun setupPageButtons(){
        bPrevious.isClickable = false
        bNext.isClickable = false
        bPrevious.setOnClickListener { gamesModel.changePage(false) }
        bNext.setOnClickListener { gamesModel.changePage(true) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(isChangingConfigurations) return

        Log.v(TAG, "**** SAVING GamesViewModel to Bundle!!!")
        outState.putParcelable(GAME_LIST_VIEW_STATE, gamesModel)
    }
}