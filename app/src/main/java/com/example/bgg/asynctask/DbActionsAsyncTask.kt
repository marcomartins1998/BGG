package com.example.bgg.asynctask

import android.os.AsyncTask

class DbActionsAsyncTask(val doTask: () -> Unit, val onSuccess: () -> Unit) : AsyncTask<String, Unit, Unit>() {
    override fun doInBackground(vararg response: String?) {
        doTask()
    }
    override fun onPostExecute(result: Unit) = onSuccess()
}