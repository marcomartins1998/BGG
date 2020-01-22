package com.example.bgg


import android.content.Intent
import android.os.Looper
import androidx.lifecycle.ViewModelProviders
import com.example.bgg.model.Game
import com.example.bgg.resources.BASE_URL
import com.example.bgg.viewmodel.GamesViewModel
import com.example.bgg.viewmodel.GamesViewModelProviderFactory
import org.awaitility.kotlin.await
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import java.util.concurrent.CompletableFuture


@RunWith(RobolectricTestRunner::class)
class GamesViewModelTest {

    @Test
    fun testSearchGamesByName() {
        /**
         *
         * Arrange
         *
         * **/
        val ctrMain = Robolectric.buildActivity(MainActivity::class.java).setup()
        val intent = Intent(ctrMain.get(), GameListActivity::class.java)
        intent.putExtra(GET_URL, "$BASE_URL&name=Catan")
        intent.putExtra(TITLE, "Catan")

        val ctr = Robolectric.buildActivity(GameListActivity::class.java, intent).setup()
        val factory = GamesViewModelProviderFactory(null, ctr.get())
        val model = ViewModelProviders.of(ctr.get(), factory)[GamesViewModel::class.java]
        val cf = CompletableFuture<ArrayList<Game>>()
        model.observe(ctr.get()) {
            println("LiveData notified!!!")
            if (it.isNotEmpty())
                cf.complete(it)
        }

        /**
         *
         * Act
         *
         * **/

        //model.getGames("${BASE_URL}&name=catan")
        await.pollInSameThread().until {
            shadowOf(Looper.getMainLooper()).idle()
            cf.isDone
        }
        assertEquals("OIXt3DmJU0", cf.get()[0].id)
    }

    @Test
    fun testSearchGamesByCompany() {
        val ctrMain = Robolectric.buildActivity(MainActivity::class.java).setup()
        val intent = Intent(ctrMain.get(), GameListActivity::class.java)
        intent.putExtra(GET_URL, "$BASE_URL&publisher=Hasbro")
        intent.putExtra(TITLE, "Hasbro")

        val ctr = Robolectric.buildActivity(GameListActivity::class.java,intent).setup()
        val factory = GamesViewModelProviderFactory(null,ctr.get())
        val model = ViewModelProviders.of(ctr.get(),factory)[GamesViewModel::class.java]

        val cf = CompletableFuture<ArrayList<Game>>()

        model.observe(ctr.get()){
            println("LiveData notified!!!")
            if(it.isNotEmpty())
                cf.complete(it)
        }

        await.pollInSameThread().until{
            shadowOf(Looper.getMainLooper()).idle()
            cf.isDone
        }
        assertEquals("UI0oxncplG", cf.get()[0].id)
        assertEquals("Scrabble", cf.get()[0].name)
    }

    @Test
    fun testSearchByCreator(){
        val ctrMain = Robolectric.buildActivity(MainActivity::class.java).setup()
        val intent = Intent(ctrMain.get(),GameListActivity::class.java)
        intent.putExtra(GET_URL, "$BASE_URL&designer=Klaus+Teuber")
        intent.putExtra(TITLE, "Klaus Teuber")

        val ctr = Robolectric.buildActivity(GameListActivity::class.java,intent).setup()
        val factory = GamesViewModelProviderFactory(null,ctr.get())
        val model = ViewModelProviders.of(ctr.get(),factory)[GamesViewModel::class.java]

        val cf = CompletableFuture<ArrayList<Game>>()

        model.observe(ctr.get()){
            if(it.isNotEmpty())
                cf.complete(it)
        }

        await.pollInSameThread().until{
            shadowOf(Looper.getMainLooper()).idle()
            cf.isDone
        }

        assertEquals("OIXt3DmJU0", cf.get()[0].id)
        assertEquals("Catan", cf.get()[0].name)

    }
}