package de.dertyp7214.rboardthemecreator

import android.app.Application
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.dertyp7214.logs.helpers.Logger
import com.downloader.PRDownloader
import de.dertyp7214.colorutilsc.ColorUtilsC
import de.dertyp7214.rboardcomponents.utils.ThemeUtils
import de.dertyp7214.rboardthemecreator.core.isReachable
import de.dertyp7214.rboardthemecreator.utils.doInBackground
import java.net.URL

class Application : Application() {

    companion object {
        var context: Application? = null
            private set

        var uiHandler: Handler? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        ColorUtilsC.init()
        ThemeUtils.registerActivityLifecycleCallbacks(this)
        ThemeUtils.applyTheme(this)
        PRDownloader.initialize(this)
        doInBackground {
            if (!URL("https://bin.utwitch.net").isReachable())
                Logger.customBin = "hastebin.com"
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit { putString("logMode", if (BuildConfig.DEBUG) "VERBOSE" else "ERROR") }
        Logger.init(this)

        context = this
        uiHandler = Handler(Looper.getMainLooper())
        PreferenceManager.getDefaultSharedPreferences(this).apply {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                when (getString("app_theme", "system_theme")) {
                    "dark" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    "light" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    "system_theme" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
            } else
                when (getString("app_theme", "light")) {
                    "dark" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    "light" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
        }
    }
}