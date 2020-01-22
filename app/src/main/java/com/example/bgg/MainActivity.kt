package com.example.bgg

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bgg.resources.BASE_URL

const val TAG = "BGG APP"
const val GET_URL = "GET_URL"
const val TITLE = "TITLE"

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    val etSearchInput: TextView by lazy { findViewById<TextView>(R.id.etSearchInput) }
    val bByName: Button by lazy { findViewById<Button>(R.id.bSearchByName) }
    val bByCompany: Button by lazy { findViewById<Button>(R.id.bSearchByCompany) }
    val bByCreator: Button by lazy { findViewById<Button>(R.id.bSearchByCreator) }
    val bByPopularity: Button by lazy { findViewById<Button>(R.id.bSearchByPopularity) }
    val bManageLists: Button by lazy { findViewById<Button>(R.id.bManageLists) }
    val btFavourite: Button by lazy { findViewById<Button>(R.id.bFavourite) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.lifecycle.addObserver(LifecycleLogger(componentName.className))
        setupButtons()
    }

    fun setupButtons(){
        bByName.setOnClickListener {
            checkInput {
                val intent = Intent(this, GameListActivity::class.java)
                intent.putExtra(GET_URL, "$BASE_URL&name=${etSearchInput.text.replace(" ".toRegex(), "+")}")
                intent.putExtra(TITLE, etSearchInput.text.toString())
                startActivity(intent)
            }
        }

        bByCompany.setOnClickListener {
            checkInput {
                val intent = Intent(this, GameListActivity::class.java)
                intent.putExtra(GET_URL, "$BASE_URL&publisher=${etSearchInput.text.replace(" ".toRegex(), "+")}")
                intent.putExtra(TITLE, etSearchInput.text.toString())
                startActivity(intent)
            }
        }

        bByCreator.setOnClickListener {
            checkInput {
                val intent = Intent(this, GameListActivity::class.java)
                intent.putExtra(GET_URL, "$BASE_URL&designer=${etSearchInput.text.replace(" ".toRegex(), "+")}")
                intent.putExtra(TITLE, etSearchInput.text.toString())
                startActivity(intent)
            }
        }

        bByPopularity.setOnClickListener {
            val intent = Intent(this, GameListActivity::class.java)
            intent.putExtra(GET_URL, "$BASE_URL&order_by=popularity")
            intent.putExtra(TITLE, "Popularity")
            startActivity(intent)
        }

        bManageLists.setOnClickListener {
            val intent = Intent(this, ListManagingActivity::class.java)
            startActivity(intent)
        }

        btFavourite.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
        }
    }

    fun checkInput(onSuccess: () -> Unit){
        if(!etSearchInput.text.isNullOrBlank()) onSuccess()
        else Toast.makeText(this, "Please fill the input box!", Toast.LENGTH_SHORT).show()
    }
}
