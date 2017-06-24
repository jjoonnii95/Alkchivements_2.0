package de.daschubbm.alkchievements20

import android.app.Application
import android.content.Context

/**
 * Created by Maxi on 22.06.2017.
 */
class App: Application() {
    companion object {
        var context: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}