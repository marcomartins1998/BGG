package com.example.bgg.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.BGGApp
import com.example.bgg.CustomListActivity
import com.example.bgg.R
import com.example.bgg.asynctask.DbActionsAsyncTask
import com.example.bgg.viewmodel.CustomListsViewModel

const val CUSTOM_LIST_NAME = "CUSTOM_LIST_NAME"

class CustomListsViewAdapter(private val context: Context, private val customListsModel: CustomListsViewModel) : RecyclerView.Adapter<CustomListsViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val customListView = LayoutInflater.from(parent.context).inflate(R.layout.layout_custom_list_view, parent, false)
        return ViewHolder(customListView)
    }

    override fun getItemCount(): Int = customListsModel.customLists.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.tvCustomListName.text = customListsModel.customLists[pos].name
        holder.bViewList.setOnClickListener {
            val intent = Intent(context, CustomListActivity::class.java)
            intent.putExtra(CUSTOM_LIST_NAME, customListsModel.customLists[pos].name)
            context.startActivity(intent)
        }
        holder.bRemoveList.setOnClickListener {
            DbActionsAsyncTask({
                BGGApp.boardGamesDb.listDao().delete(customListsModel.customLists[pos])
            }, {}).execute()
        }
    }

    class ViewHolder(
        itemView: View,
        val tvCustomListName: TextView = itemView.findViewById(R.id.tvCustomListName),
        val bViewList: Button = itemView.findViewById(R.id.bCustomList),
        val bRemoveList: Button = itemView.findViewById(R.id.bCustomListRemove)
    ) : RecyclerView.ViewHolder(itemView)
}