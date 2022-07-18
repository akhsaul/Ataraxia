package com.gkkendor.app.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gkkendor.app.R
import com.gkkendor.app.databinding.ActivityMainBinding
import com.gkkendor.app.util.getNavController
import com.gkkendor.app.util.openWhatsapp
import com.gkkendor.app.util.removeBackground
import com.gkkendor.app.util.toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .putExtra(Intent.EXTRA_MIME_TYPES, Constants.acceptedMimeTypes)
        */
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)

        val navView: BottomNavigationView = binding.navView.removeBackground()
        //setupFirebase()

        getNavController(R.id.nav_host_fragment_activity_main)?.let {
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            setupActionBarWithNavController(
                it,
                AppBarConfiguration(
                    setOf(
                        R.id.navigation_home,
                        R.id.navigation_news,
                        R.id.navigation_report,
                        R.id.navigation_setting
                    )
                )
            )
            navView.setupWithNavController(it)
        }
    }

    /*
    private fun setupFirebase() {
        //TODO
        // Firebase or GCM (Google Cloud Messaging)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            )
        }

        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }

        logToken()

        binding.btnPanic.setOnClickListener {
            toast("Panic Button Clicked")
            Log.d(TAG, "Subscribing to Ataraxia topic")
            // [START subscribe_topics]
            Firebase.messaging.subscribeToTopic(getString(R.string.default_notification_channel_name))
                .addOnCompleteListener { task ->
                    var msg = getString(R.string.msg_subscribed)
                    if (!task.isSuccessful) {
                        msg = getString(R.string.msg_subscribe_failed)
                    }
                    Log.d(TAG, msg)
                    toast(msg)
                }
        }
    }

    private fun logToken() {
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            toast(msg)
        }
    }
    */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_support -> {
                Log.i(TAG, "Support menu clicked")
                val phoneNumber = "6285156452059"
                val message = StringBuilder("Halo")
                openWhatsapp(phoneNumber, message.toString()) {
                    Log.i(TAG, it)
                }
            }
            R.id.menu_notifications -> {
                toast("Notification menu clicked")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}