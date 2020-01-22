package com.example.bgg

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bgg.adapter.DesignersAdapter
import com.example.bgg.resources.BASE_URL
import com.example.bgg.viewmodel.GameDetailsViewModelProviderFactory
import com.example.bgg.viewmodel.GameViewModel

const val GAME_VIEW_STATE = "GAME_VIEW_STATE"
const val GAME = "game"

class GameDetailsActivity : AppCompatActivity() {
    val TAG = "GameDetailsActivity"

    lateinit var gameModel: GameViewModel
    val rvDesigners: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rvDesigners) }
    val designersAdapter: DesignersAdapter by lazy { DesignersAdapter(this, if(!gameModel.game.designers.isEmpty()) gameModel.game.designers else arrayOf("No Data")) }

    fun initGameDetailsViewModel(savedInstanceState: Bundle?): GameViewModel{
        val factory = GameDetailsViewModelProviderFactory(savedInstanceState, this.intent)
        return ViewModelProviders.of(this, factory)[GameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)
        this.lifecycle.addObserver(LifecycleLogger(componentName.className))
        gameModel = initGameDetailsViewModel(savedInstanceState)
        setupImage()
        setupFields()
        setupPrimaryPublisher()
        setupDesignerList()
    }

    private fun setupImage(){
        Glide.with(this).load(gameModel.game.image_url).override(200, 300).fitCenter().into(findViewById(R.id.imageURL))
        findViewById<ImageView>(R.id.imageURL).setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(gameModel.game.url)
            startActivity(intent)
        }
    }

    private fun setupFields(){
        findViewById<TextView>(R.id.tvName).text = gameModel.game.name
        findViewById<TextView>(R.id.tvMinPlayers).text = gameModel.game.min_players
        findViewById<TextView>(R.id.tvMaxPlayers).text = gameModel.game.max_players
        findViewById<TextView>(R.id.tvDescription).text = gameModel.game.description_preview
        findViewById<TextView>(R.id.tvMinAge).text = gameModel.game.min_age
        findViewById<TextView>(R.id.tvAverageUserRating).text = gameModel.game.average_user_rating.toString()
        findViewById<TextView>(R.id.tvRulesUrl).text = gameModel.game.rules_url
        findViewById<TextView>(R.id.tvRulesUrl).movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupPrimaryPublisher(){
        val primaryPublisher: TextView = findViewById(R.id.tvPrimaryPublisher)
        primaryPublisher.text = gameModel.game.primary_publisher
        if(gameModel.game.primary_publisher == "No Data"){
            findViewById<Button>(R.id.bPrimaryPublisher).isClickable = false
            findViewById<Button>(R.id.bPrimaryPublisher).visibility = View.GONE
        } else {
            findViewById<Button>(R.id.bPrimaryPublisher).setOnClickListener {
                val intent = Intent(this, GameListActivity::class.java)
                intent.putExtra(GET_URL, "$BASE_URL&publisher=${gameModel.game.primary_publisher.replace(" ".toRegex(), "+")}")
                Log.d(gameModel.game.primary_publisher,TAG)
                intent.putExtra(TITLE, gameModel.game.primary_publisher)
                startActivity(intent)
            }
        }
    }

    private fun setupDesignerList(){
        rvDesigners.layoutManager = LinearLayoutManager(this)
        rvDesigners.adapter = designersAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(isChangingConfigurations) return

        Log.v(TAG, "**** SAVING GameViewModel to Bundle!!!")
        outState.putParcelable(GAME_VIEW_STATE, gameModel)
    }
}