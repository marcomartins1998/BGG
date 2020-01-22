package com.example.bgg.asynctask

import android.os.AsyncTask

class FavouritesAsyncTask(val doTask: () -> Long, val onSuccess: (Long) -> Unit) : AsyncTask<String, Unit, Long>() {
    override fun doInBackground(vararg response: String?): Long {
        return doTask()
    }

    override fun onPostExecute(result: Long) = onSuccess(result)
}