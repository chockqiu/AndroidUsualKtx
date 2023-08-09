package com.xy.xframework.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Process
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.security.MessageDigest
import java.util.*

object ContextExt {

    fun Context.openBrowser(url: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    /**
     * 网络是否可用
     */
    @SuppressLint("MissingPermission")
    fun Context.networkAvailable(): Boolean {
        try {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            val wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (networkInfo != null) {
                if (wifi != null && wifi.isConnected) {
                    return true
                }
                if (mobile != null && mobile.isConnected) {
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun Context.widthPixels(): Int {
        return this.resources.displayMetrics.widthPixels
    }

    fun Context.heightPixels(): Int {
        return this.resources.displayMetrics.heightPixels
    }

    fun Context.isDestroyed(): Boolean {
        return when (this) {
            is FragmentActivity -> {
                this.isDestroyed
            }
            is Activity -> {
                this.isDestroyed
            }
            is Application -> {
                false
            }
            else -> false
        }
    }

    /**
     * 获得屏幕宽度 px
     *
     * @return
     */
    fun Context.getScreenWidth(): Int {
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 获得屏幕高度 px
     *
     * @return
     */
    fun Context.getScreenHeight(): Int {
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    fun Context.getAppVersionName(): String {
        return try {
            val pm = packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.versionName ?: ""
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    fun Context.getAppVersionCode(): Int {
        return try {
            val pm = packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.versionCode ?: 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }
    }

    fun Context.sha1(): String? {
        try {
            val info = this.packageManager.getPackageInfo(
                this.packageName, PackageManager.GET_SIGNATURES
            )
            val cert = info.signatures[0].toByteArray()
            val md = MessageDigest.getInstance("SHA1")
            val publicKey = md.digest(cert)
            val hexString = StringBuffer()
            for (i in publicKey.indices) {
                val appendString = Integer.toHexString(
                    0xFF and publicKey[i]
                        .toInt()
                )
                    .toUpperCase(Locale.US)
                if (appendString.length == 1) hexString.append("0")
                hexString.append(appendString)
                hexString.append(":")
            }
            val result = hexString.toString()
            return result.substring(0, result.length - 1)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取当前主进程的packageName
     */
    fun Context.getCurrentProcessName(): String {
        var processName = ""
        try {
            val pid = Process.myPid()
            val manager =
                this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val list = manager.runningAppProcesses
            if (list != null && list.isNotEmpty()) {
                for (process in list) {
                    if (process.pid == pid) {
                        processName = process.processName
                        break
                    }
                }
            }

        } catch (e: java.lang.Exception) {
            e.message
        }
        return processName
    }

    fun Context.getManifestString(name: String?): String? {
        try {
            val appInfo =
                this.packageManager.getApplicationInfo(
                    this.packageName,
                    PackageManager.GET_META_DATA
                )
            return appInfo.metaData.getString(name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 校验context
     */
    fun Context?.checkContext(): Boolean {
        if (this == null) return false
        if (this is Activity) {
            if (this.isDestroyed) {
                return false
            }
        }
        if (this is Fragment) {
            if (this.isDetached) {
                return false
            }
        }
        return true
    }
}