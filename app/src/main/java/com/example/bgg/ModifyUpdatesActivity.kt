package com.example.bgg

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.bgg.worker.SHARED_PREFERENCES
import com.example.bgg.worker.WorkerFavouriteGames
import java.util.concurrent.TimeUnit

class ModifyUpdatesActivity : AppCompatActivity() {
    val TAG = "ModifyUpdatesActivity"
    val sp: SharedPreferences by lazy { this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE) }

    val sMobileData: Switch by lazy { findViewById<Switch>(R.id.sMobileData) }
    val sPhoneCharging: Switch by lazy { findViewById<Switch>(R.id.sPhoneCharging) }
    val sLowBattery: Switch by lazy { findViewById<Switch>(R.id.sLowBattery) }
    val sbTimePeriod: SeekBar by lazy { findViewById<SeekBar>(R.id.sbTimePeriod) }
    val tvTimePeriod: TextView by lazy { findViewById<TextView>(R.id.tvTimePeriod) }
    val bModifyConstraints: Button by lazy { findViewById<Button>(R.id.bModifyConstraints) }

    val min = 15
    val max = 60
    val step = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_updates)
        this.lifecycle.addObserver(LifecycleLogger(componentName.className))
        setupSeekBar()
        setupButton()
        setupPreviousSettings()
    }

    fun setupSeekBar(){
        sbTimePeriod.max = (max - min)/step
        sbTimePeriod.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val progress_custom = min + (progress * step)
                tvTimePeriod.text = progress_custom.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        sbTimePeriod.progress = 0
    }

    fun setupButton(){
        bModifyConstraints.setOnClickListener {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(if(sMobileData.isChecked) NetworkType.CONNECTED else NetworkType.UNMETERED)
                .setRequiresCharging(sPhoneCharging.isChecked)
                .setRequiresBatteryNotLow(!sLowBattery.isChecked)
                .build()
            val request = PeriodicWorkRequestBuilder<WorkerFavouriteGames>((min + (sbTimePeriod.progress * step)).toLong(), TimeUnit.MINUTES)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .addTag(WORKER_TAG)
                .build()
            WorkManager.getInstance(applicationContext).cancelAllWorkByTag(WORKER_TAG)
            WorkManager.getInstance(applicationContext).enqueue(request)
            val editor = sp.edit()
            editor.putString(WORKER_CONSTRAINTS, "${if(sMobileData.isChecked) CONNECTED else UNMETERED};${sPhoneCharging.isChecked};${!sLowBattery.isChecked};${sbTimePeriod.progress}")
            editor.apply()
            Toast.makeText(this, resources.getString(R.string.updates_modified_successfully), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun setupPreviousSettings(){
        val splitStr = sp.getString(WORKER_CONSTRAINTS, "$CONNECTED;false;false;15")!!.split(";")
        sMobileData.isChecked = (splitStr[0] == CONNECTED)
        sPhoneCharging.isChecked = splitStr[1].toBoolean()
        sLowBattery.isChecked = !splitStr[2].toBoolean()
        sbTimePeriod.progress = splitStr[3].toInt()
    }
}
