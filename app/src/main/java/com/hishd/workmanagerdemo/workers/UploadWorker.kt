package com.hishd.workmanagerdemo.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hishd.workmanagerdemo.MainActivity

class UploadWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
        return try {

            //Capture the data which is sent by the sender or the starter
            val count = inputData.getInt(MainActivity.KEY_COUNT_VALUE, 0)

            for(i in 0..count)
                Log.i("UploadWorker", "Uploading ${i}")

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}