package com.hishd.workmanagerdemo.workers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hishd.workmanagerdemo.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class UploadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    //Create the keys to pass the results to the caller or the starter
    companion object {
        const val KEY_WORKER = "UploadWorker"
    }

    override fun doWork(): Result {
        return try {

            val count: Int? = null

            //Capture the data which is sent by the sender or the starter
            if (inputData.hasKeyWithValueOfType(MainActivity.KEY_COUNT_VALUE, Int::class.java)) {
                val count = inputData.getInt(MainActivity.KEY_COUNT_VALUE, 0)
            }

            for (i in 0 until 600)
                Log.i("UploadWorker", "Uploading ${i}")

            //Sending the current date to the caller or starter
            val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ENGLISH)
            val currentDate = dateFormat.format(Date())

            val data = Data.Builder().apply {
                this.putString(KEY_WORKER, currentDate.toString())
            }.build()

//            Result.success()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure()
        }
    }
}