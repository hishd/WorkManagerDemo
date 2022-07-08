package com.hishd.workmanagerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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
        val workManager = WorkManager.getInstance(applicationContext)
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java).build()
        workManager.enqueue(uploadRequest)

        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observe(this) {
            findViewById<TextView>(R.id.textView).text = it.state.name
        }
    }
}