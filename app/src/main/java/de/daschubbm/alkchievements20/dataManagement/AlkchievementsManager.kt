package de.daschubbm.alkchievements20.dataManagement

import android.util.Log
import de.daschubbm.alkchievements20.App
import de.daschubbm.alkchievements20.R
import de.daschubbm.alkchievements20.control.Events
import de.daschubbm.alkchievements20.util.readJson

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

                val alkchievementObj = Alkchievement(id, name, desc)
                allAlkchievements.put(id, alkchievementObj)
            }

            Events.addHandler("Drink-Returned") { args ->
                val user = args[0] as Person
                val prevReturns = LocalData.loadI("drinksReturned", 0)
                LocalData.saveI("drinksReturned", prevReturns + 1)

                val wurstfinger = allAlkchievements["wurstfinger"]

                wurstfinger?.let { triggerIfLevelUp(it, user, prevReturns + 1) }
            }
        } catch (e: NullPointerException) {
            Log.e("AC-AM-init", "App Context not ready!??!?")
        }
    }

    private fun  triggerIfLevelUp(alkchievement: Alkchievement, user: Person, newVal: Int) {
        alkchievement.levels.asReversed() //As reversed so that the highest level reached gets triggered
                .filter {
                    newVal >= it && user.alkchievements[alkchievement] ?: Int.MAX_VALUE < it //it: Level
                }
                .forEach { //Trigger!
                    Log.d("AC-AM-trigger", "Trigger!!")
                    Events.trigger("Alkchievement-Level-Up", listOf(alkchievement, it))
                    return
                }
    }
}