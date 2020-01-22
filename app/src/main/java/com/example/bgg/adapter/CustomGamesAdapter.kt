package com.example.bgg.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bgg.GAME
import com.example.bgg.GameDetailsActivity
import com.example.bgg.R
import com.example.bgg.viewmodel.CustomGamesViewModel
import java.math.RoundingMode

class CustomGamesAdapter(private val gamesViewModel: CustomGamesViewModel) :
    RecyclerView.Adapter<CustomGamesAdapter.ViewHolder>() {
    val TAG = "CustomGamesAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val gameView = LayoutInflater.from(parent.context).inflate(R.layout.layout_simple_game, parent, false)
        return ViewHolder(gameView)
    }

    override fun getItemCount(): Int = gamesViewModel.games.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.tvGameName.text = gamesViewModel.games[pos].name
        holder.tvAverageRating.text = gamesViewModel.games[pos].average_user_rating.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toPlainString()
        Glide.with(holder.itemView)
            .load(gamesViewModel.games[pos].thumb_url)
            .override(200, 300)
            .fitCenter().into(holder.ivThumbnail)

        holder.bGetDetails.setOnClickListener {
            val intent = Intent(it.context, GameDetailsActivity::class.java)
            intent.putExtra(GAME, gamesViewModel.games[pos])
            it.context.startActivity(intent)
        }
    }

    class ViewHolder(
        itemView: View,
        val ivThumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail),
        val tvGameName: TextView = itemView.findViewById(R.id.tvGameName),
        val tvAverageRating: TextView = itemView.findViewById(R.id.tvAverageRating),
        val bGetDetails: Button = itemView.findViewById(R.id.bGetDetails)
    ) : RecyclerView.ViewHolder(itemView)

}