package com.example.bgg

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.adapter.CustomListsAddToAdapter
import com.example.bgg.viewmodel.CustomListsViewModel
import com.example.bgg.viewmodel.GameDetailsViewModelProviderFactory
import com.example.bgg.viewmodel.GameViewModel

const val GAME_ID = "GAME_ID"

class AddToCustomListActivity : AppCompatActivity() {
    val TAG = "ATCustomListActivity"

    val rvCustomLists: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rvCustomLists) }
    val customListsAdapter: CustomListsAddToAdapter by lazy { CustomListsAddToAdapter(this, customListsModel, gameViewModel) }
    val customListsModel: CustomListsViewModel by lazy { ViewModelProviders.of(this)[CustomListsViewModel::class.java] }
    lateinit var gameViewModel: GameViewModel

    fun initGameViewModel(savedInstanceState: Bundle?): GameViewModel{
        val factory = GameDetailsViewModelProviderFactory(savedInstanceState, this.intent)
        return ViewModelProviders.of(this, factory)[GameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_custom_list)
        this.lifecycle.addObserver(LifecycleLogger(componentName.className))
        gameViewModel = initGameViewModel(savedInstanceState)
        rvCustomLists.layoutManager = LinearLayoutManager(this)
        rvCustomLists.adapter = customListsAdapter
        customListsModel.observe(this){
            customListsAdapter.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(isChangingConfigurations) return

        Log.v(TAG, "**** SAVING GameViewModel to Bundle!!!")
        outState.putParcelable(GAME_VIEW_STATE, gameViewModel)
    }
}
