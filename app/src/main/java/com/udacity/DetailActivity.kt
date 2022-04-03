package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.toolbar
import kotlinx.android.synthetic.main.content_detail.filename_value_tv
import kotlinx.android.synthetic.main.content_detail.ok_button
import kotlinx.android.synthetic.main.content_detail.status_value_tv

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        // This activity will displayed only after user has tapped in the notification,
        // so we can cancel the notification.
        cancelNotification(this)

        val projectName = intent.getStringExtra(EXTRA_PROJECT_NAME)
        val downloadStatus = intent.getStringExtra(EXTRA_DOWNLOAD_STATUS)

        filename_value_tv.text = projectName
        status_value_tv.text = downloadStatus

        ok_button.setOnClickListener {
            // Go back to initial activity
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
