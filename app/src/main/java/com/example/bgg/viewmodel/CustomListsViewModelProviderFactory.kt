package com.example.bgg.viewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bgg.TAG

class CustomListsViewModelProviderFactory(val savedInstanceState: Bundle?, val intent: Intent) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass == CustomListsViewModel::class.java) {
            Log.v(TAG, "**** CREATED CustomListsViewModel from the scratch!!!")
            val customModel = CustomListsViewModel()
            return customModel as T
        } else throw IllegalArgumentException("There is no ViewModel for class $modelClass")
    }
}

