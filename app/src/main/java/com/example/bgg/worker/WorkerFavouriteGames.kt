package com.example.bgg.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.bgg.*
import com.example.bgg.adapter.NAME
import com.example.bgg.asynctask.DbActionsAsyncTask
import com.example.bgg.model.Favourite
import com.example.bgg.model.FavouriteGame
import com.example.bgg.model.Game
import com.example.bgg.resources.formatFavouriteUrl
import java.util.concurrent.CompletableFuture

const val SHARED_PREFERENCES = "SHARED_PREFERENCES"
const val SUCCESS_FAVOURITES = "SUCCESS_FAVOURITES"
const val NOTIFICATION_ID = 100012

class WorkerFavouriteGames(val context : Context, val workerParams: WorkerParameters) : Worker(context, workerParams) {
    val sp = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    override fun doWork(): Result {
        val arrSuccFavs: List<String>? = sp.getString(SUCCESS_FAVOURITES, null)?.split(";")
        var favourites : List<Favourite> = BGGApp.boardGamesDb.favouriteDao().getAllSync()
        if(arrSuccFavs != null) favourites = favourites.filter { !arrSuccFavs.contains(it.name) }
        val cfArr = arrayListOf<CompletableFuture<Boolean>>()
        favourites.forEach {
            //if(it.name == "Name2"){ BGGApp.boardGamesDb.favouriteGameDao().delete(FavouriteGame("Name2", "kPDxpJZ8PD")) }
            val cf = CompletableFuture<Boolean>()
            val favourite = it
            BGGApp.boardGamesApi.searchGames(formatFavouriteUrl(it), { reqGames ->
                Log.v(TAG, "Get games from ${favourite.name} list")
                //Do notification if is different
                DbActionsAsyncTask({
                    val favouriteGames: List<Game> = BGGApp.boardGamesDb.favouriteGameDao().getGamesWithFavouriteName(favourite.name)
                    if(favouriteGames.size != reqGames.size && favouriteGames.intersect(reqGames).size != favouriteGames.size) notifyUpdatedFavourite(favourite)
                    val ls = reqGames.filter { !favouriteGames.any { og -> it.id == og.id } }
                    BGGApp.boardGamesDb.gameDao().insertAll(*ls.toTypedArray())
                    BGGApp.boardGamesDb.favouriteGameDao().insertAll(*ls.map { ng -> FavouriteGame(it.name, ng.id) }.toTypedArray())
                }){cf.complete(true)}.execute()
            }){
                cf.complete(false)
            }
            cfArr.add(cf)
        }
        CompletableFuture.allOf(*cfArr.toTypedArray()).join()
        if(cfArr.any { !it.join() }){
            var i = 0
            var str = ""
            while(i < favourites.size){
                if (!cfArr[i].join()) str = "$str;${favourites[i].name}"
            }
            val editor = sp.edit()
            editor.putString(SUCCESS_FAVOURITES, str)
            editor.commit()
            return Result.retry()
        }
        else {
            val editor = sp.edit()
            editor.remove(SUCCESS_FAVOURITES)
            editor.commit()
            return Result.success()
        }
    }

    private fun notifyUpdatedFavourite(favourite: Favourite) {
        val intent = Intent(applicationContext, ListFavouriteGamesActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra(NAME, favourite.name)
        intent.putExtra(GET_URL, formatFavouriteUrl(favourite))

        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(favourite.name)
            .setContentText("Favourite: ${favourite.name} has been updated.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat
            .from(applicationContext)
            .notify(NOTIFICATION_ID, notification)
    }

}

