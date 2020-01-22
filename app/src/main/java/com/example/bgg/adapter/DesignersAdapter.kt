package com.example.bgg.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.GET_URL
import com.example.bgg.GameListActivity
import com.example.bgg.R
import com.example.bgg.TITLE
import com.example.bgg.resources.BASE_URL

class DesignersAdapter(private val context: Context, private val designerList: Array<String>) : RecyclerView.Adapter<DesignersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val designerView = LayoutInflater.from(parent.context).inflate(R.layout.layout_designer, parent, false)
        return ViewHolder(designerView)
    }

    override fun getItemCount(): Int = designerList.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.tvName.text = designerList[pos]
        if(designerList[pos] == "No Data"){
            holder.bDesigner.isClickable = false
            holder.bDesigner.visibility = View.GONE
        }
        else {
            holder.bDesigner.setOnClickListener {
                val intent = Intent(context, GameListActivity::class.java)
                intent.putExtra(GET_URL, "$BASE_URL&designer=${designerList[pos].replace(" ".toRegex(), "+")}")
                println("$BASE_URL&designer=${designerList[pos].replace(" ".toRegex(), "+")}")
                intent.putExtra(TITLE, designerList[pos])
                context.startActivity(intent)
            }
        }
    }

    class ViewHolder(
        itemView: View,
        val tvName: TextView = itemView.findViewById(R.id.tvName),
        val bDesigner: Button = itemView.findViewById(R.id.bDesigner)
    ) : RecyclerView.ViewHolder(itemView)
}