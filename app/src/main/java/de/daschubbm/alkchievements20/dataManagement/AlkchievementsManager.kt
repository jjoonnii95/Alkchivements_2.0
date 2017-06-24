package de.daschubbm.alkchievements20.dataManagement

import android.util.Log
import de.daschubbm.alkchievements20.App
import de.daschubbm.alkchievements20.R
import de.daschubbm.alkchievements20.util.readJson

/**
 * Created by Maxi on 22.06.2017.
 */
object AlkchievementsManager {
    val allAlkchievements = mutableMapOf<String, Alkchievement>()

    init {
        try {
            val json = readJson(App.Companion.context!!, R.raw.alkchievements)

            val alkchievements = json.getJSONArray("alkchievements")

            for (i in 0..alkchievements.length() - 1) {
                val alkchievement = alkchievements.getJSONObject(i)
                val id = alkchievement.getString("id")
                val name = alkchievement.getString("name")
                val desc = alkchievement.getString("desc")

                val alkchievementObj = Alkchievement(id, name, id, desc)
                allAlkchievements.put(id, alkchievementObj)
            }
        } catch (e: NullPointerException) {
            Log.e("AC-AM-init", "App Context not ready!??!?")
        }
    }
}