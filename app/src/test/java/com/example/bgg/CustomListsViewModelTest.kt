package com.example.bgg

import androidx.lifecycle.ViewModelProviders
import com.example.bgg.model.CustomList
import com.example.bgg.viewmodel.CustomListsViewModel
import junit.framework.Assert
import org.awaitility.kotlin.await
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CompletableFuture

@RunWith(RobolectricTestRunner::class)
class CustomListsViewModelTest {

    @Test
    fun testCustomListsViewModel(){
        val ctr = Robolectric.buildActivity(ListManagingActivity::class.java).setup()
        val model = ViewModelProviders.of(ctr.get())[CustomListsViewModel::class.java]
        val list = CustomList("List")
        asyncInsert(list)

        val finish = CompletableFuture<Unit>()
        model.observe(ctr.get()){
            println("LiveData notified!!!")
            if (it.isNotEmpty()) {
                Assert.assertEquals("List", it.get(0).name)
                finish.complete(null)
            }
        }

        await.pollInSameThread().until {
            Robolectric.flushForegroundThreadScheduler()
            finish.isDone
        }

    }

    fun asyncInsert(list: CustomList) =
        CompletableFuture.supplyAsync {
            println("Thread ${Thread.currentThread().hashCode()}")
            BGGApp.boardGamesDb.listDao().insertAll(list)
        }
}