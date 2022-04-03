package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.custom_button
import kotlinx.android.synthetic.main.content_main.download_options_rg
import java.security.InvalidParameterException

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    private var project: Project? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            handleButtonClick()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(this)
        }

        notificationManager = ContextCompat.getSystemService(
            this, NotificationManager::class.java) as NotificationManager
    }

    private fun handleButtonClick() {
        val selectedId = download_options_rg.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(selectedId)
        if (radioButton == null) {
            Toast.makeText(this, getString(R.string.download_select_file), Toast.LENGTH_SHORT)
                .show()
        } else {
            project = getProject(selectedId)
            project?.let {
                download(it.url)
            }
        }
    }

    private fun getProject(selectedId: Int): Project {
        return when (selectedId) {
            R.id.download_glide_rb -> Project(
                "https://github.com/bumptech/glide/archive/refs/heads/master.zip",
                    getString(R.string.download_option_glide))
            R.id.download_udacity_rb -> Project(
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip",
                getString(R.string.download_option_udacity))
            R.id.download_retrofit_rb -> Project(
                "https://github.com/square/retrofit/archive/refs/heads/master.zip",
                getString(R.string.download_option_retrofit))
            else -> throw InvalidParameterException("Invalid option")
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                // Get download status
                val action = intent.action
                val status = if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                    getString(R.string.download_success) else getString(R.string.download_fail)

                // Send notification warning that the download is complete
                notificationManager.sendNotification(
                    applicationContext,
                    project?.name ?: "",
                    status)
            }
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }
}

data class Project(val url: String, val name: String)