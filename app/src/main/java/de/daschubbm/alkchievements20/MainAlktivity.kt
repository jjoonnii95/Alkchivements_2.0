package de.daschubbm.alkchievements20

import android.content.ComponentCallbacks2
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.View
import de.daschubbm.alkchievements20.control.Events
import de.daschubbm.alkchievements20.control.Notifications
import de.daschubbm.alkchievements20.dataManagement.FirebaseManager
import de.daschubbm.alkchievements20.dataManagement.LocalData
import de.daschubbm.alkchievements20.dataManagement.Person
import de.daschubbm.alkchievements20.mainAlktivity.DrinksAlkdapter
import kotlinx.android.synthetic.main.alktivity_main.*

class MainAlktivity : AppCompatActivity() {

    lateinit var user: Person
        get

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alktivity_main)

        Events.addHandler("FirebaseReady") { ->
            FirebaseManager.getPerson(LocalData.loadS("name")) { p ->
                user = p ?: throw IllegalStateException("User does not exist!!")

                drinksView.layoutManager = LinearLayoutManager(this)
                drinksView.adapter = DrinksAlkdapter(FirebaseManager.allCategories.values, user)

                drinksView.visibility = View.VISIBLE
                loadSpinner.visibility = View.GONE
            }
        }

        FirebaseManager.toString() // Invoke object initializer
        Notifications.toString()
    }
}
