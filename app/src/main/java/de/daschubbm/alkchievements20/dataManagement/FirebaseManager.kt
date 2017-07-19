package de.daschubbm.alkchievements20.dataManagement

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.daschubbm.alkchievements20.control.Events

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

                        Events.trigger("FirebaseReady")
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

    fun consumeDrink(user: Person, drink: String, callback: () -> Unit) {
        interactWithDrink(user, drink, callback, true)
    }

    fun returnDrink(user: Person, drink: String, callback: () -> Unit) {
        interactWithDrink(user, drink, callback, false)
    }

    private fun interactWithDrink(user: Person, drink: String, callback: () -> Unit, consume: Boolean) {
        val drinkObj = allDrinks[drink] ?: return

        val updatedValues = mutableMapOf<String, Int>()

        val diff = if (consume) 1 else -1
        val drunk = user.drinks[drinkObj] ?: 0

        if (drunk == 0 && !consume) { // Can't give back a drink when you have 0
            callback.invoke()
            return
        }
        if (drinkObj.stock == 0 && consume) { //Can't drink a drink if none are there
            callback.invoke()
            return
        }

        updatedValues["people/${user.name}/drinks/${drinkObj.name}"] = drunk + diff
        updatedValues["drinks/${drinkObj.name}/stock"] = drinkObj.stock - diff

        db.reference.updateChildren(updatedValues as Map<String, Any>?)
        callback.invoke()
    }
}