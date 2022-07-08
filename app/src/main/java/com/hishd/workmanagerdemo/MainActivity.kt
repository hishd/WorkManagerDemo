package com.hishd.workmanagerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.work.*
import com.hishd.workmanagerdemo.workers.UploadWorker

class MainActivity : AppCompatActivity() {

    //Create the companion object to use the keys
    companion object {
        const val KEY_COUNT_VALUE = "key_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            setOneTimeWorkRequest()
        }
    }

    private fun setOneTimeWorkRequest() {
        val workManager = WorkManager.getInstance(applicationContext)

        //Setting constraints to the work manager to start when the device is charging
        //Setting constraints to the work manager to start when the device is connected to internet
//        val constraints = Constraints.Builder().apply {
////            this.setRequiresCharging(true)
//            this.setRequiredNetworkType(NetworkType.CONNECTED)
//        }.build()

        //Passing values to the worker object
        val data = Data.Builder().apply {
            this.putInt(KEY_COUNT_VALUE, 150)
        }.build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
//            .setConstraints(constraints)
            .setInputData(data)
            .build()
        workManager.enqueue(uploadRequest)

        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observe(this) {
            findViewById<TextView>(R.id.textView).text = it.state.name
        }
    }
}