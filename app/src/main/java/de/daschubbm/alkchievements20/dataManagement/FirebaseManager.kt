package de.daschubbm.alkchievements20.dataManagement

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseManager {
    val db: FirebaseDatabase = FirebaseDatabase.getInstance()

    val allDrinks = mutableMapOf<String, Drink>()
    val allCategories = mutableMapOf<String, DrinkCategory>()

    init {
        val drinksRef = db.getReference("drinks")

        drinksRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError?) {
                println(error?.message)
                System.exit(1)
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                for (drink in snapshot?.children ?: emptyList()) {
                    val drinkObj = Drink(drinksRef.child(drink.key), drink)
                    allDrinks.put(drink.key, drinkObj)
                }

                /*********************************************************
                 *      Finished drink loading, load categories now      *
                 *********************************************************/

                val catsRef = db.getReference("categories")

                catsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {
                        println(error?.message)
                        System.exit(1)
                    }

                    override fun onDataChange(snapshot: DataSnapshot?) {
                        for (cat in snapshot?.children ?: emptyList()) {
                            val catObj = DrinkCategory(catsRef.child(cat.key), cat)
                            allCategories.put(cat.key, catObj)
                        }
                    }
                })
            }
        })
    }

    fun getPerson(name: String, callback: (Person?) -> Unit) {
        val personRef = db.getReference("people/" + name)

        personRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError?) {
                println(error?.message)
                System.exit(1)
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                val personObject = snapshot?.let { Person(personRef, it) }
                callback.invoke(personObject)
            }
        })
    }
}