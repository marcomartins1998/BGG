package com.example.bgg

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.bgg.asynctask.FavouriteGamesAsyncTask
import com.example.bgg.dto.GameDto
import com.example.bgg.model.Favourite
import com.example.bgg.model.FavouriteGame
import com.example.bgg.model.Game
import com.example.bgg.resources.arrayOfGames
import junit.framework.Assert.assertEquals
import org.awaitility.kotlin.await
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode
import java.util.concurrent.CompletableFuture

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class FavouriteGameDaoTest {

    @Test
    fun testFavouriteGameDao(){
        val ctr = Robolectric.buildActivity(MainActivity::class.java).setup()
        insertArrayOfGames()
        asyncInsertFavourite()
        asyncInsertFavouriteGameDao()
        val all: MutableLiveData<List<Game>> = MutableLiveData(emptyList())

        val finish_get = CompletableFuture<Unit>()
        val task = FavouriteGamesAsyncTask(BGGApp.boardGamesDb) {
            all.value = it
            finish_get.complete(null)
        }
        task.execute("Favourite")

        val finish_observe = CompletableFuture<Unit>()
        all.observe(ctr.get(), Observer{
            if(it.isNotEmpty()){
                assertEquals("Monopoly: European Edition",it.get(0).name)
                assertEquals("5SjD0ZuSyA",it.get(0).id)
                finish_observe.complete(null)
            }
        })

        await.pollInSameThread().until {
            //Robolectric.flushForegroundThreadScheduler()
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            finish_get.isDone && finish_observe.isDone
        }

    }

    fun insertArrayOfGames() {
        val game = arrayOfGames().map(GameDto::toGame)
        println("Thread ${Thread.currentThread().hashCode()}")
        asyncInsertGame(*game.toTypedArray())
    }

    fun asyncInsertGame(vararg game: Game) {
        CompletableFuture.supplyAsync {
            println("Thread ${Thread.currentThread().hashCode()}")
            BGGApp.boardGamesDb.gameDao().insertAll(*game)
        }.join()
    }

    fun asyncInsertFavourite() {
        CompletableFuture.supplyAsync {
            val favourite = Favourite("Favourite","","","","")
            println("Thread ${Thread.currentThread().hashCode()}")
            BGGApp.boardGamesDb.favouriteDao().insertFavourite(favourite)
        }.join()
    }

    fun asyncInsertFavouriteGameDao() {
        CompletableFuture.supplyAsync {
            val games = arrayOfGames().map(GameDto::toGame)
            println("Thread ${Thread.currentThread().hashCode()}")
            BGGApp.boardGamesDb.favouriteGameDao().insertAll(*games.map{ FavouriteGame("Favourite", it.id) }.toTypedArray())
        }.join()
    }
}


