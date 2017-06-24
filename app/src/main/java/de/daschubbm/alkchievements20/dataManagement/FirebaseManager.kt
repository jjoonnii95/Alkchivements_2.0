package de.daschubbm.alkchievements20.dataManagement

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.daschubbm.alkchievements20.dataManagement.AlkchievementsManager.allAlkchievements

object FirebaseManager {
    val db = FirebaseDatabase.getInstance()

    val allDrinks = mutableMapOf<String, Drink>()
    val allCategories = mutableMapOf<String, DrinkCategory>()

    init {
        val drinksRef = db.getReference("drinks")

        drinksRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError?) {
                println(error?.message)
                System.exit(1)
            }

            override fun onDataChange(person: DataSnapshot?) {
                for (drink in person?.children!!) {
                    val name = drink.key
                    val price = drink.child("price").value as Short
                    val stock = drink.child("stock").value as Int
                    val iconImgRes = drink.child("iconImgRes").value as String
                    val categoryName = drink.child("category").value as String

                    val drinkObj = Drink(name, price, stock, iconImgRes)
                    allDrinks.put(name, drinkObj)

                    if (categoryName !in allCategories.keys) {
                        val category = DrinkCategory(categoryName, mutableListOf<Drink>())
                        category.drinks.add(drinkObj)
                    } else {
                        allCategories[categoryName]!!.drinks.add(drinkObj)
                    }
                }
            }
        })
    }

    fun getPerson(name: String, callback: (Person) -> Unit) {
        val personRef = db.getReference("people/" + name)

        personRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError?) {
                println(error?.message)
                System.exit(1)
            }

            override fun onDataChange(person: DataSnapshot?) {
                val appVersion = person?.child("appVersion")?.value as Int
                val drinks = mutableMapOf<Drink, Int>()

                for (drink in person.child("drinks").children) {
                    val drinkName = drink.key
                    val drinksHad = drink.value as Int

                    if (drinkName !in allDrinks.keys) continue

                    try {
                        drinks.put(allDrinks[drinkName]!!, drinksHad)
                    } catch (e: NullPointerException) {
                        Log.wtf("AC-FM-getPerson", "$name has a drink named $drinkName which is not registered within the app.")
                    }
                }

                val alkchievements = mutableMapOf<Alkchievement, Int>()

                for (alkchievement in person.child("achievements").children) {
                    val alkchievementId = alkchievement.key
                    val alkchievementLevel = alkchievement.value as Int

                    try {
                        alkchievements.put(allAlkchievements[alkchievementId]!!, alkchievementLevel)
                    } catch (e: NullPointerException) {
                        Log.wtf("AC-FM-getPerson", "$name has an alkchievement named $alkchievementId which is not registered within the app.")
                    }
                }

                val personObject = Person(name, appVersion, drinks, alkchievements, name)
                callback.invoke(personObject)
            }
        })
    }
}