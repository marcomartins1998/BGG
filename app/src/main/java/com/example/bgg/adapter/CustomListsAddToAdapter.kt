package com.example.bgg.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.BGGApp
import com.example.bgg.R
import com.example.bgg.asynctask.DbActionsAsyncTask
import com.example.bgg.model.CustomListGame
import com.example.bgg.viewmodel.CustomListsViewModel
import com.example.bgg.viewmodel.GameViewModel

class CustomListsAddToAdapter(private val activity: Activity, private val customListsModel: CustomListsViewModel, private val gameModel: GameViewModel) : RecyclerView.Adapter<CustomListsAddToAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val customListView = LayoutInflater.from(parent.context).inflate(R.layout.layout_custom_list_addto, parent, false)
        return ViewHolder(customListView)
    }

    override fun getItemCount(): Int = customListsModel.customLists.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.tvCustomListName.text = customListsModel.customLists[pos].name
        holder.bAddToList.setOnClickListener {
            val doTask = {
                BGGApp.boardGamesDb.gameDao().insertAll(gameModel.game)
                BGGApp.boardGamesDb.listGameDao().insertAll(CustomListGame(customListsModel.customLists[pos].name, gameModel.game.id))
            }
            val onSuccess = {
                Toast.makeText(activity, activity.resources.getString(R.string.custom_list_add_success), Toast.LENGTH_SHORT).show()
                activity.finish()
            }
            DbActionsAsyncTask(doTask, onSuccess).execute()
        }
    }

    class ViewHolder(
        itemView: View,
        val tvCustomListName: TextView = itemView.findViewById(R.id.tvCustomListName),
        val bAddToList: Button = itemView.findViewById(R.id.bCustomList)
    ) : RecyclerView.ViewHolder(itemView)
}