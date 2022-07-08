package com.hishd.workmanagerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.hishd.workmanagerdemo.workers.UploadWorker

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            setOneTimeWorkRequest()
        }
    }

    private fun setOneTimeWorkRequest() {
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java).build()
        WorkManager.getInstance(applicationContext)
            .enqueue(uploadRequest)
    }
}