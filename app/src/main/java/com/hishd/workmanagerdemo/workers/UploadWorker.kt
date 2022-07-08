package com.hishd.workmanagerdemo.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
        return try {
            for(i in 0..600)
                Log.i("UploadWorker", "Uploading ${i}")

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}