package com.example.bgg

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.bgg.model.Favourite
import com.example.bgg.model.Game
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
class FavouriteRepositoryTest {
    @Test
    fun testFavouriteRepositoryTest(){
        asyncInsertFavourite()
        val ctr = Robolectric.buildActivity(MainActivity::class.java).setup()
        val name = "Favourite"
        val url = "https://www.boardgameatlas.com/api/search?client_id=p2q1KFQiUy&designer=Klaus+Teuber&publishers=Catan+Studio&mechanics=R0bGq4cAl4&categories=7rV11PKqME"
        val all: LiveData<List<Game>> = BGGApp.favouriteRepository.findByName(name, url)

        val finish = CompletableFuture<Unit>()
        all.observe(ctr.get(), Observer {
            println("LiveData notified!!!")
            if(it != null && it.isNotEmpty()){
                assertEquals("Catan",it.get(0).name)
                assertEquals("OIXt3DmJU0",it.get(0).id)
                finish.complete(null)
            }
        })

        await.pollInSameThread().until {
            //Robolectric.flushForegroundThreadScheduler()
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            finish.isDone
        }
    }

    fun asyncInsertFavourite(){
        CompletableFuture.supplyAsync {
            val favourite = Favourite("Favourite","","","","")
            println("Thread ${Thread.currentThread().hashCode()}")
            BGGApp.boardGamesDb.favouriteDao().insertFavourite(favourite)
        }
    }
}