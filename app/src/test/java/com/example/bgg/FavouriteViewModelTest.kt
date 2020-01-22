package com.example.bgg

import androidx.lifecycle.ViewModelProviders
import com.example.bgg.model.Favourite
import com.example.bgg.viewmodel.FavouriteViewModel
import junit.framework.Assert.assertEquals
import org.awaitility.kotlin.await
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CompletableFuture

@RunWith(RobolectricTestRunner::class)
class FavouriteViewModelTest {

    @Test
    fun testFavouriteViewModel(){
        val ctr = Robolectric.buildActivity(FavouriteActivity::class.java).setup()
        val model = ViewModelProviders.of(ctr.get())[FavouriteViewModel::class.java]
        val favourite = Favourite("Favourite","Klaus Teuber","Catan Studio","Mechanic","Category")
        asyncInsert(favourite)

        val finish = CompletableFuture<Unit>()
        model.observe(ctr.get()){
            println("LiveData notified!!!")
            if (it.isNotEmpty()) {
                assertEquals("Favourite", it.get(0).name)
                assertEquals("Klaus Teuber", it.get(0).designer)
                finish.complete(null)
            }
        }

        await.pollInSameThread().until {
            Robolectric.flushForegroundThreadScheduler()
            finish.isDone
        }

    }

    fun asyncInsert(favourite : Favourite) =
        CompletableFuture.supplyAsync {
            println("Thread ${Thread.currentThread().hashCode()}")
            BGGApp.boardGamesDb.favouriteDao().insertFavourite(favourite)
        }
}