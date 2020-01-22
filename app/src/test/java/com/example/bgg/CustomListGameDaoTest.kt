package com.example.bgg

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.bgg.dto.GameDto
import com.example.bgg.model.CustomList
import com.example.bgg.model.CustomListGame
import com.example.bgg.model.Game
import com.example.bgg.resources.arrayOfGames
import junit.framework.Assert.assertEquals
import org.awaitility.kotlin.await
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import java.util.concurrent.CompletableFuture

@RunWith(RobolectricTestRunner::class)
class CustomListGameDaoTest {

    @Test
    fun testCustomListDaoTest(){


        val ctr = Robolectric.buildActivity(MainActivity::class.java).setup()

        insertArrayOfGames()
        insertCustomList().join()
        insertCustomListGame().join()


        val all : LiveData<List<Game>> = BGGApp.boardGamesDb.listGameDao().getGamesWithListName("List")

        val finish = CompletableFuture<Unit>()
        all.observe(ctr.get(), Observer{

            if(it.isNotEmpty()){
                val game = it.filter {it.name == "Catan"}[0]
                assertEquals("Catan",game.name)
                assertEquals("OIXt3DmJU0", game.id)
                finish.complete(null)
            }
        })


        await.pollInSameThread().until {
            // Robolectric.flushForegroundThreadScheduler()
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            finish.isDone
        }


    }

    fun insertCustomList() =
        CompletableFuture.supplyAsync{
            println("Thread ${Thread.currentThread().hashCode()}")
            BGGApp.boardGamesDb.listDao().insertAll(CustomList("List"))
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
        }
    }


    fun insertCustomListGame() =
        CompletableFuture.supplyAsync{
            val games = arrayOfGames().map(GameDto::toGame)
            println("Thread ${Thread.currentThread().hashCode()}")
            BGGApp.boardGamesDb.listGameDao().insertAll(*games.map { CustomListGame("List",it.id) }.toTypedArray())

        }



}