package com.hishd.workmanagerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.work.*
import com.hishd.workmanagerdemo.workers.UploadWorker

class MainActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager

    //Create the companion object to use the keys
    companion object {
        const val KEY_COUNT_VALUE = "key_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.workManager = WorkManager.getInstance(applicationContext)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            setOneTimeWorkRequest()
        }
    }

    private fun setOneTimeWorkRequestWithConstants() {
        //Setting constraints to the work manager to start when the device is charging
        //Setting constraints to the work manager to start when the device is connected to internet
        val constraints = Constraints.Builder().apply {
//            this.setRequiresCharging(true)
            this.setRequiredNetworkType(NetworkType.CONNECTED)
        }.build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .build()

        startRequest(uploadRequest)
    }

    private fun setOneTimeWorkRequestWithInputData() {
        //Passing values to the worker object
        val data = Data.Builder().apply {
            this.putInt(KEY_COUNT_VALUE, 150)
        }.build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setInputData(data)
            .build()

        startRequest(uploadRequest)
    }

    private fun setOneTimeWorkRequest() {
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .build()

        startRequest(uploadRequest)
    }

    private fun startRequest(request: WorkRequest) {
        workManager.enqueue(request)
        workManager.getWorkInfoByIdLiveData(request.id).observe(this) {
            findViewById<TextView>(R.id.textView).text = it.state.name

            //Receive the data sent from worker object
            if(it.state.isFinished) {
                val data = it.outputData
                val message = data.getString(UploadWorker.KEY_WORKER)
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}