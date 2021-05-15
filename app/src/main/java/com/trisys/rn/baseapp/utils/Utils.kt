package com.trisys.rn.baseapp.utils

import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.text.Layout
import android.util.Log
import android.util.TypedValue
import com.trisys.rn.baseapp.BuildConfig
import com.trisys.rn.baseapp.helper.TextDrawable
import org.ocpsoft.prettytime.PrettyTime
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getAppVersionCode(context: Context): Int? {
        var versonName: Int? = 0
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versonName = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versonName
    }
    fun getAppVersionName(context: Context): String? {
        var versonName: String? = ""
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versonName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versonName
    }

    fun getLocaleTime(context: Context, dateStr: String): String {
        var formattedDate = dateStr
        try {
            if (dateStr.contains("ago") || dateStr.contains("now")) {
                return dateStr
            }
            val dfSource = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
            val srcDate = dfSource.parse(dateStr)
            formattedDate = getPrettyTime(srcDate.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return formattedDate
    }
    fun getPrettyTime(ms: Long): String {
        val p = PrettyTime()
        val date = Date(ms)
        return p.format(date)
    }

    fun getFontDrawable(context: Context, icon: String, color: String, size: Float): TextDrawable {

        val faIcon = TextDrawable(context)
        faIcon.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
        faIcon.setTextColor(Color.parseColor(color))
        faIcon.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        faIcon.setTypeface(FontManager.getTypeface(context, FontManager.FONTAWESOME))
        faIcon.setText(icon)

        return faIcon
    }
    fun getFontDrawableIcon(context: Context, icon: String, color: Int, size: Float): TextDrawable {

        val faIcon = TextDrawable(context)
        faIcon.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
        faIcon.setTextColor(color)
        faIcon.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        faIcon.setTypeface(FontManager.getTypeface(context, FontManager.FONTAWESOME))
        faIcon.setText(icon)

        return faIcon
    }

    //Read JSON File from assert folder to return String
    fun loadJSONFromAsset(context: Context, jsonFileName: String): String? {
        var json: String? = null
        try {

            val `is` = context.assets.open("$jsonFileName.json")

            val size = `is`.available()

            val buffer = ByteArray(size)

            `is`.read(buffer)

            `is`.close()

            json = String(buffer)


        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json

    }

    fun loadUrl(activity: Context, url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        activity.startActivity(i)
    }

    fun isAppInstalled(context: Context, packageName: String): Boolean {
        try {
            context.packageManager.getApplicationInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

    }
    fun log(tag:String, message:String?){
        if(BuildConfig.DEBUG){
            Log.d(tag,message!!)
        }
    }
}