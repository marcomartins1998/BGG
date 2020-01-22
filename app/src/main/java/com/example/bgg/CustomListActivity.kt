package com.example.bgg

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.adapter.CUSTOM_LIST_NAME
import com.example.bgg.adapter.CustomGamesAdapter
import com.example.bgg.viewmodel.CustomGamesViewModel
import com.example.bgg.viewmodel.CustomGamesViewModelProviderFactory

class CustomListActivity : AppCompatActivity() {
    val TAG = "CustomListActivity"

    val tvTitle: TextView by lazy { findViewById<TextView>(R.id.tvTitle) }
    val rvCustom: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rvCustom) }

    val gamesAdapter: CustomGamesAdapter by lazy { CustomGamesAdapter(customGamesModel) }
    lateinit var customGamesModel: CustomGamesViewModel

    fun initCustomGamesViewModel(savedInstanceState: Bundle?): CustomGamesViewModel{
        val factory = CustomGamesViewModelProviderFactory(savedInstanceState, this.intent)
        return ViewModelProviders.of(this, factory)[CustomGamesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_list)
        this.lifecycle.addObserver(LifecycleLogger(componentName.className))
        customGamesModel =  initCustomGamesViewModel(savedInstanceState)
        customGamesModel.observe(this){
            gamesAdapter.notifyDataSetChanged()
        }
        rvCustom.layoutManager = LinearLayoutManager(this)
        rvCustom.adapter = gamesAdapter
        tvTitle.text = customGamesModel.listname
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(isChangingConfigurations) return

        Log.v(TAG, "**** SAVING GamesViewModel to Bundle!!!")
        outState.putString(CUSTOM_LIST_NAME, customGamesModel.listname)
    }

}





