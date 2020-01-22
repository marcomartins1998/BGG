package com.example.bgg.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.BGGApp
import com.example.bgg.GET_URL
import com.example.bgg.ListFavouriteGamesActivity
import com.example.bgg.R
import com.example.bgg.asynctask.DbActionsAsyncTask
import com.example.bgg.resources.formatFavouriteUrl
import com.example.bgg.viewmodel.FavouriteViewModel

const val NAME = "GET_NAME"

class FavouritesAdapter (private val favouriteViewModel: FavouriteViewModel) : RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val favouritesView = LayoutInflater.from(parent.context).inflate(R.layout.layout_favourite, parent, false)
        return ViewHolder(favouritesView)
    }

    override fun getItemCount(): Int = favouriteViewModel.favourites.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNameFav.text = favouriteViewModel.favourites[position].name
        holder.btSearchFav.setOnClickListener{
            val url = formatFavouriteUrl(favouriteViewModel.favourites[position])
            val name = favouriteViewModel.favourites[position].name
            val intent = Intent(it.context, ListFavouriteGamesActivity::class.java)
            intent.putExtra(NAME,name)
            intent.putExtra(GET_URL,url)
            it.context.startActivity(intent)
        }
        holder.btRemoveFavourite.setOnClickListener{
            DbActionsAsyncTask({
                BGGApp.boardGamesDb.favouriteDao().delete(favouriteViewModel.favourites[position])
            },{}).execute()
        }
        holder.tvDesigner.text = favouriteViewModel.favourites[position].designer
        holder.tvPublisher.text = favouriteViewModel.favourites[position].publisher
        holder.tvMechanic.text = favouriteViewModel.favourites[position].mechanic
        holder.tvCategory.text = favouriteViewModel.favourites[position].category
        holder.ivDropdown.setOnClickListener {
            if(holder.clDropdown.visibility == View.GONE) holder.clDropdown.visibility = View.VISIBLE
            else holder.clDropdown.visibility = View.GONE
        }

    }

    class ViewHolder (
        itemView: View,
        val tvNameFav : TextView = itemView.findViewById(R.id.tvNameFav),
        val btSearchFav : Button = itemView.findViewById(R.id.btSearchFav),
        val btRemoveFavourite : Button = itemView.findViewById(R.id.btRemoveFavourite),
        val ivDropdown : ImageView = itemView.findViewById(R.id.ivDropdown),
        val clDropdown : ConstraintLayout = itemView.findViewById(R.id.clDropdown),
        val tvDesigner : TextView = itemView.findViewById(R.id.tvDesigner),
        val tvPublisher : TextView = itemView.findViewById(R.id.tvPublisher),
        val tvMechanic : TextView = itemView.findViewById(R.id.tvMechanic),
        val tvCategory : TextView = itemView.findViewById(R.id.tvCategory)

    ): RecyclerView.ViewHolder(itemView)


}