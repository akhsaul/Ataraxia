package com.gkkendor.app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gkkendor.app.R
import java.text.SimpleDateFormat

fun <T : View> T.removeBackground() = apply {
    this.background = null
}

fun FragmentActivity.getNavHostFragment(@IdRes id: Int): NavHostFragment? {
    return supportFragmentManager.findFragmentById(id) as? NavHostFragment
}

fun FragmentActivity.getNavController(@IdRes id: Int): NavController? {
    return getNavHostFragment(id)?.navController
}

@JvmOverloads
fun Context?.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (this != null) {
        require(duration in 0..1) {
            "Value must be 0 or 1"
        }
        Toast.makeText(this, message, duration).show()
    }
}

@JvmOverloads
fun Context?.toast(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
    this?.toast(getString(id), duration)
}

fun Context?.isAppInstalled(packageName: String): Boolean {
    return if (this == null) {
        false
    } else {
        runCatching {
            /* same like
            * if(pm != null) pm.getPackageInfo() else return false
            * */
            this.packageManager?.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                ?: return false
            return true
        }.getOrDefault(false)
    }
}

/**
 * return NotificationManager if Context is not null
 * */
fun Context.createNotificationChannel(): NotificationManager {
    val notificationManager: NotificationManager =
        this.getSystemService(NotificationManager::class.java)
    // Since android Oreo notification channel is needed.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                getString(R.string.default_notification_channel_id),
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setAllowBubbles(true)
                enableLights(true)
                enableVibration(true)
            }
        )
    }
    return notificationManager
}

fun Context?.openWhatsapp(
    phoneNumber: String,
    message: String,
    log: ((message: String) -> Any)? = null
) {
    if (this != null) {
        val firstChar = phoneNumber[0]

        val validNumber = if (firstChar == '+' || firstChar == '0') {
            phoneNumber.substring(1, phoneNumber.length)
        } else {
            phoneNumber
        }

        runCatching {
            val intent = if (isAppInstalled("com.whatsapp")) {
                log?.invoke("Move intent with app, $phoneNumber, $message")
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("whatsapp://send?phone=$validNumber&text=$message")
                ).setPackage("com.whatsapp")
            } else {
                log?.invoke("Move intent with browser, $phoneNumber, $message")
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=$validNumber&text=$message")
                )
            }
            startActivity(intent)

            // can be opened in browser
            //Uri.parse("https://api.whatsapp.com/send?phone=+6285156452059&text=Halo")
            // can not be opened in browser
            //Uri.parse("whatsapp://send?phone=+6285156452059&text=Halo")
            /*
            val intent = Intent(Intent.ACTION_SEND).setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, "Halo")
                .putExtra("jid", "$smsNumber@s.whatsapp.net") //phone number without "+" prefix
                .setPackage("com.whatsapp")
             */
        }.getOrElse {
            toast("Error")
        }
    }
}

fun String.formatTo(format: SimpleDateFormat): String {
    return runCatching {
        val dateFormat = SimpleDateFormat(Constants.PATTERN_DATE_RESPONSE)
        val date = dateFormat.parse(this)
        format.format(date!!)
    }.onFailure {
        Log.e(
            "Date Formatter",
            "String can't parse with pattern ${Constants.PATTERN_DATE_RESPONSE}"
        )
    }.getOrDefault(this)
}