package com.gkkendor.app.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

fun <T: View> T.removeBackground() = apply {
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