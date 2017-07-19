package de.daschubbm.alkchievements20.control

import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.app.NotificationCompat
import de.daschubbm.alkchievements20.App
import de.daschubbm.alkchievements20.R
import de.daschubbm.alkchievements20.dataManagement.Alkchievement
import de.daschubbm.alkchievements20.util.drawableIdToBitmap

const val GROUP_ALKCHIEVEMENTS = "alkchievements"

object Notifications {

    val manager: NotificationManager = App.context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    var currentId =336629

    init {
        Events.addHandler("Alkchievement-Level-Up") { args ->
            val alkchievement = args[0] as Alkchievement
            val level = args[1] as Int

            val desc = alkchievement.description.replace(Regex("%.*?%"), level.toString())

            val drawableId = App.context!!.resources.getIdentifier(alkchievement.identifier, "drawable", App.context!!.packageName)

            val notification = NotificationCompat.Builder(App.context)
                    .setContentTitle("Du hast ${alkchievement.name} freigeschaltet!")
                    .setContentText(desc)
                    .setSmallIcon(R.drawable.plus)
                    .setGroup(GROUP_ALKCHIEVEMENTS)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(kotlin.LongArray(0))
                    .setLargeIcon(drawableIdToBitmap(drawableId, App.context!!))
                    .build()

            manager.notify(currentId++, notification)
        }
    }
}