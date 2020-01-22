package com.example.bgg

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.work.*
import com.example.bgg.api.BoardGamesWebApi
import com.example.bgg.model.BoardGamesDb
import com.example.bgg.model.FavouriteRepository
import com.example.bgg.worker.SHARED_PREFERENCES
import com.example.bgg.worker.WorkerFavouriteGames
import java.util.concurrent.TimeUnit

const val CHANNEL_ID = "123"
const val WORKER_TAG = "UPDATE_FAVOURITES"
const val WORKER_CONSTRAINTS = "WORKER_CONSTRAINTS"
const val CONNECTED = "CONNECTED"
const val UNMETERED = "UNMETERED"

class BGGApp : Application() {
    companion object {
        lateinit var boardGamesApi: BoardGamesWebApi
        lateinit var boardGamesDb: BoardGamesDb
        lateinit var favouriteRepository: FavouriteRepository
    }

    override fun onCreate() {
        super.onCreate()
        boardGamesApi = BoardGamesWebApi(applicationContext)
        boardGamesDb = Room.databaseBuilder(applicationContext, BoardGamesDb::class.java, "boardgames-db").build()
        favouriteRepository = FavouriteRepository(boardGamesApi, boardGamesDb)
        createNotificationChannel()
        scheduleBackgroundWork()
    }

    private fun scheduleBackgroundWork() {
        val sp = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        if(!sp.contains(WORKER_CONSTRAINTS)){
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(false)
                .build()
            val request = PeriodicWorkRequestBuilder<WorkerFavouriteGames>(15, TimeUnit.MINUTES)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .addTag(WORKER_TAG)
                .build()
            try {
                WorkManager.getInstance(applicationContext)
            } catch (e: IllegalStateException) {
                WorkManager.initialize(applicationContext, Configuration.Builder().setMinimumLoggingLevel(android.util.Log.INFO).build())
            }
            WorkManager.getInstance(applicationContext).enqueue(request)
            val editor = sp.edit()
            editor.putString(WORKER_CONSTRAINTS,"$CONNECTED;false;false;0")
            editor.apply()
        }
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}