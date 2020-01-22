package com.example.bgg

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.bgg.dto.GameDto
import com.example.bgg.model.Game
import com.example.bgg.resources.arrayOfGames
import org.awaitility.kotlin.await
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CompletableFuture

@RunWith(RobolectricTestRunner::class)
class GameDaoTest {

    @Test
    fun testQueryGetAllGameDao(){
        val ctr = Robolectric.buildActivity(MainActivity::class.java).setup()
        val all : LiveData<List<Game>> = BGGApp.boardGamesDb.gameDao().getAll()
        val game = arrayOfGames().map(GameDto::toGame)
        asyncInsert(*game.toTypedArray())

        val finish = CompletableFuture<Unit>()
        all.observe(ctr.get(),Observer{
            if(it.isNotEmpty()) {
                assertEquals(it.get(0).id,"EL3YmDLY6W")
                assertEquals(it.get(0).name,"Risk")
                assertEquals(it.get(1).id,"M7UpZZ00UO")
                assertEquals(it.get(1).name,"Risk: Legacy")
                finish.complete((null))
            }
        })

        await.pollInSameThread().until {
            Robolectric.flushForegroundThreadScheduler()
            finish.isDone
        }
    }

    @Test
    fun testQueryFindByNameGameDao(){
        val ctr = Robolectric.buildActivity(MainActivity::class.java).setup()
        val game : LiveData<List<Game>> = BGGApp.boardGamesDb.gameDao().findByName("EL3YmDLY6W")

        insertArrayOfGames()

        val finish =  CompletableFuture<Unit>()
        game.observe(ctr.get(),Observer{
            if(it.isNotEmpty()){
                assertEquals("Risk",it.get(0).name)
                finish.complete(null)
            }
        })

        await.pollInSameThread().until {
            Robolectric.flushForegroundThreadScheduler()
            //Shadows.shadowOf(Looper.getMainLooper()).idle()
            finish.isDone
        }

    }


    fun insertArrayOfGames(){
        val game = arrayOfGames().map(GameDto::toGame)
        println("Thread ${Thread.currentThread().hashCode()}")
        asyncInsert(*game.toTypedArray())
    }

    fun asyncInsert(vararg game: Game) =
        CompletableFuture.supplyAsync {
            println("Thread ${Thread.currentThread().hashCode()}")
            BGGApp.boardGamesDb.gameDao().insertAll(*game)
        }
}