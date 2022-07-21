package com.gkkendor.app.ui.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gkkendor.app.R
import com.gkkendor.app.databinding.ActivityMainBinding
import com.gkkendor.app.model.FCMMessage
import com.gkkendor.app.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    private var isPanicActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .putExtra(Intent.EXTRA_MIME_TYPES, Constants.acceptedMimeTypes)
        */
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        FirebaseMessaging.getInstance().subscribeToTopic("ataraxia")
        val locationManager = getSystemService(LocationManager::class.java)
        binding.btnPanic.setOnClickListener {
            isPanicActive = !isPanicActive
            if (isPanicActive) {
                sendMessageFCM(locationManager)
            } else {
                listener?.let {
                    locationManager.removeUpdates(it)
                }
                okhttpRequest(Constants.toJson(
                    FCMMessage(
                        data = mapOf(
                            "isPanicActive" to "false",
                            "emailSender" to Constants.email
                        )
                    )
                ))
            }
        }
        setSupportActionBar(binding.materialToolbar)

        val navView: BottomNavigationView = binding.navView.removeBackground()
        //setupFirebase()

        getNavController(R.id.nav_host_fragment_activity_main)?.let { navController ->
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            setupActionBarWithNavController(
                navController, AppBarConfiguration(
                    setOf(
                        R.id.homeFragment,
                        R.id.articleFragment,
                        R.id.reportFragment,
                        R.id.settingFragment
                    )
                )
            )
            navView.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.detailReportFragment
                    || destination.id == R.id.detailArticleFragment
                ) {
                    visibilityBottomLayout(View.GONE)
                } else {
                    visibilityBottomLayout(View.VISIBLE)
                }
            }
            navView.setOnItemSelectedListener { item ->

                Log.d(TAG, "Current destination ${translate(navController.currentDestination?.id)}")

                navController.currentDestination?.id?.let { id ->
                    if (id == R.id.detailReportFragment
                        || id == R.id.detailArticleFragment
                    ) {
                        Log.d(TAG, "Pop ${translate(id)}")
                        navController.popBackStack(id, true)
                    }
                }

                Log.d(TAG, "Navigate item ${translate(item.itemId)}")
                NavigationUI.onNavDestinationSelected(item, navController)
                return@setOnItemSelectedListener true
            }
            navView.setOnItemReselectedListener { item ->
                Log.d(TAG, "Pop by reselected ${item.itemId}")
                navController.popBackStack(item.itemId, inclusive = false)
            }
        }
    }

    private var listener: LocationListener? = null

    private fun makeLocationListener() {
        if (listener == null) {
            listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    sendLocationToFCM(location)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
            IntentFilter("MyData")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            /*
            awaitingDriverRequesting.dismiss()
            awaitingDriverPhone.setText(intent.extras!!.getString("phone")) //setting values to the TextViews
            awaitingDriverLat.setText(intent.extras!!.getDouble("lat"))
            awaitingDriverLng.setText(intent.extras!!.getDouble("lng"))
             */
        }
    }

    private fun sendLocationToFCM(location: Location) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val json = Constants.toJson(
                    FCMMessage(
                        data = mapOf(
                            "isPanicActive" to "true",
                            "currentLocation" to location.toString(),
                            "emailSender" to Constants.email
                        )
                    )
                )
                okhttpRequest(json)
            } catch (t: Throwable) {
                Log.e(TAG, "Error in sendLocationToFCM", t)
            }
        }
    }

    private fun sendMessageFCM(locationManager: LocationManager) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val provider =
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        LocationManager.GPS_PROVIDER
                    } else {
                        requireNotNull(locationManager.getBestProvider(Criteria(), true)) {
                            "There is no provider enabled"
                        }
                    }
                val currentLocation = locationManager.getLastKnownLocation(provider)?.toString()

                val json = Constants.toJson(
                    FCMMessage(
                        data = mapOf(
                            "title" to "HELP",
                            "body" to "Someone need your help!!",
                            "isPanicActive" to "true",
                            "currentLocation" to (currentLocation ?: "Unknown"),
                            "emailSender" to Constants.email
                        )
                    )
                )
                okhttpRequest(json)

                makeLocationListener()
                locationManager.requestLocationUpdates(provider, 500L, 1f, listener!!)
            } catch (t: Throwable) {
                Log.e(TAG, "Error in sendMessageFCM", t)
            }
        }
    }

    private fun okhttpRequest(json: String) {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder()
            .url(Constants.URL_FCM)
            .header("Authorization", "Bearer ${Constants.FCM_LEGACY_API}")
            .post(json.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                throw e
            }

            override fun onResponse(call: Call, response: Response) {
                response.close()
            }
        })
    }

    private fun visibilityBottomLayout(value: Int) {
        require(value == View.GONE || value == View.VISIBLE) {
            "require View.GONE or View.VISIBLE"
        }
        // this will hide FAB, BottomNav, BottomAppBar
        binding.coordinatorLayout.visibility = value
    }

    private fun translate(itemId: Int?): String {
        return if (itemId != null) {
            when (itemId) {
                R.id.homeFragment -> "Home"
                R.id.articleFragment -> "Article"
                R.id.reportFragment -> "Report"
                R.id.detailArticleFragment -> "Detail Article"
                R.id.detailReportFragment -> "Detail Report"
                else -> "Unknown $itemId"
            }
        } else {
            "NULL"
        }
    }


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
            Log.d(TAG, "Subscribing to ataraxia-help topic")
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
            android.R.id.home -> {
                // if current view is Fragment it will popBackStack
                // if current view is Activity it will back to previous activity or close the app
                onBackPressed()
            }
            else -> {
                Log.i(TAG, "itemId ${item.itemId}, tittle ${item.title}")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}