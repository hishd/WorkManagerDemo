package com.hishd.workmanagerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.work.*
import com.hishd.workmanagerdemo.workers.UploadWorker
import java.util.concurrent.TimeUnit

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

        startOneTimeRequest(uploadRequest)
    }

    private fun setOneTimeWorkRequestWithInputData() {
        //Passing values to the worker object
        val data = Data.Builder().apply {
            this.putInt(KEY_COUNT_VALUE, 150)
        }.build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setInputData(data)
            .build()

        startOneTimeRequest(uploadRequest)
    }

    private fun setOneTimeWorkRequest() {
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .build()

        startOneTimeRequest(uploadRequest)
    }

    //Start periodic request for repeating tasks
    private fun setPeriodicWorkRequest(repeatInterval: Long, timeUnit: TimeUnit) {
        val periodicWorkRequest = PeriodicWorkRequest.Builder(UploadWorker::class.java, repeatInterval, timeUnit).build()
        startRequest(periodicWorkRequest)
    }

    //Starting the passed OneTimeWorkRequest
    private fun startOneTimeRequest(request: OneTimeWorkRequest) {
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

    //Starting the passed WorkRequest
    private fun startRequest(request: WorkRequest) {
        workManager.enqueue(request)
        workManager.getWorkInfoByIdLiveData(request.id).observe(this) {
            findViewById<TextView>(R.id.textView).text = it.state.name
        }
    }

    //Chaining work requests
    private fun startMultipleRequests(request1: OneTimeWorkRequest, request2: OneTimeWorkRequest, request3: OneTimeWorkRequest) {
        workManager
            .beginWith(request1)
            .then(request2)
            .then(request3)
            .enqueue()

        workManager.getWorkInfoByIdLiveData(request1.id).observe(this) {
            findViewById<TextView>(R.id.textView).text = it.state.name
        }
    }
}