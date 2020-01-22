package com.example.bgg

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.adapter.CustomListsViewAdapter
import com.example.bgg.asynctask.CustomListsAsyncTask
import com.example.bgg.model.CustomList
import com.example.bgg.viewmodel.CustomListsViewModel

class ListManagingActivity : AppCompatActivity() {
    val TAG = "ListManagingActivity"

    val etCustomListNameInput: EditText by lazy { findViewById<EditText>(R.id.etCustomListNameInput) }
    val bCreateCustomList: Button by lazy { findViewById<Button>(R.id.bCreateCustomList) }
    val rvCustomLists: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rvCustomLists) }
    val customListsAdapter: CustomListsViewAdapter by lazy { CustomListsViewAdapter(this, customListsModel) }
    val customListsModel : CustomListsViewModel by lazy {
        ViewModelProviders.of(this)[CustomListsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_managing)
        this.lifecycle.addObserver(LifecycleLogger(componentName.className))

        setupButton()
        rvCustomLists.layoutManager = LinearLayoutManager(this)
        rvCustomLists.adapter = customListsAdapter
        customListsModel.observe(this){
            customListsAdapter.notifyDataSetChanged()
        }
    }

    fun setupButton(){
        bCreateCustomList.setOnClickListener {
            if(!etCustomListNameInput.text.isNullOrBlank()){
                val doTask = { BGGApp.boardGamesDb.listDao().insert(CustomList(etCustomListNameInput.text.toString())) }
                CustomListsAsyncTask(doTask, {
                    if(it.toInt() == -1) Toast.makeText(this, resources.getString(R.string.new_list_name_exists_error), Toast.LENGTH_SHORT).show()
                }).execute()
            } else Toast.makeText(this, resources.getString(R.string.new_list_name_error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(isChangingConfigurations) return
    }
}
