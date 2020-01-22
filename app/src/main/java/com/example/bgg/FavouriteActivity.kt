package com.example.bgg

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.OnConflictStrategy
import com.example.bgg.adapter.FavouritesAdapter
import com.example.bgg.asynctask.FavouritesAsyncTask
import com.example.bgg.model.Favourite
import com.example.bgg.viewmodel.FavouriteViewModel


class FavouriteActivity : AppCompatActivity(){

    val etNameFilter : TextView by lazy { findViewById<TextView>(R.id.etNameFilter) }
    val etDesignerFilter : TextView by lazy { findViewById<TextView>(R.id.etDesignerFilter) }
    val etPublisherFilter : TextView by lazy { findViewById<TextView>(R.id.etPublisherFilter) }
    val bCreateFilter : Button by lazy { findViewById<Button>(R.id.bCreateFilter) }
    val bModifyUpdates : Button by lazy { findViewById<Button>(R.id.bModifyUpdates) }
    val spMechanics : Spinner by lazy { findViewById<Spinner>(R.id.spMechanics) }
    val spCategories : Spinner by lazy { findViewById<Spinner>(R.id.spCategories) }
    val rvFavourite : RecyclerView by lazy {findViewById<RecyclerView>(R.id.rvFavourites)}
    val favouriteModel : FavouriteViewModel by lazy { ViewModelProviders.of(this)[FavouriteViewModel::class.java] }
    val favouriteAdapter : FavouritesAdapter by lazy { FavouritesAdapter(favouriteModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        this.lifecycle.addObserver(LifecycleLogger(componentName.className))
        setupSpinners()
        setupButtons()
        favouriteModel.observe(this) {
            favouriteAdapter.notifyDataSetChanged()
        }
        rvFavourite.layoutManager = LinearLayoutManager(this)
        rvFavourite.adapter = favouriteAdapter
    }

    fun setupSpinners(){
        ArrayAdapter.createFromResource(this,R.array.favourite_mechanics,android.R.layout.simple_spinner_item)
            .also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                spMechanics.adapter = arrayAdapter

            }

        ArrayAdapter.createFromResource(this,R.array.favourite_categories,android.R.layout.simple_spinner_item)
            .also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                spCategories.adapter = arrayAdapter
            }
    }

    fun setupButtons(){
        bCreateFilter.setOnClickListener{
            if(etNameFilter.text.toString().isNullOrBlank()) Toast.makeText(this, this.resources.getString(R.string.favourite_name_empty),Toast.LENGTH_SHORT).show()
            else {
                val doTask = {
                    BGGApp.boardGamesDb.favouriteDao().insertFavourite(
                        Favourite(
                            etNameFilter.text.toString(),
                            etDesignerFilter.text.toString(),
                            etPublisherFilter.text.toString(),
                            spMechanics.selectedItem.toString(),
                            spCategories.selectedItem.toString()
                        )
                    )
                }
                val onSuccess: (Long) -> (Unit) = {
                    println(it.toInt())
                    println(OnConflictStrategy.IGNORE)
                    if(it.toInt() == -1)
                        Toast.makeText(this, resources.getString(R.string.favourite_exists), Toast.LENGTH_SHORT).show()
                }
                FavouritesAsyncTask(doTask, onSuccess).execute()
            }
        }
        bModifyUpdates.setOnClickListener {
            val intent = Intent(this, ModifyUpdatesActivity::class.java)
            startActivity(intent)
        }
    }
}