package de.daschubbm.alkchievements20.dataManagement

import com.google.firebase.database.*
import de.daschubbm.alkchievements20.control.Events
import java.util.*

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

        val transactionEventID = "Transaction-${Math.abs(Random().nextInt())}-Completed"

        Events.addHandler(transactionEventID) { ->
            drinkObj.dbRef.runTransaction(object : Transaction.Handler {
                override fun onComplete(err: DatabaseError?, committed: Boolean, snap: DataSnapshot?) {
                    if (committed) {
                        callback.invoke()
                        Events.trigger("DrinkModified", listOf(drinkObj))
                    }
                }

                override fun doTransaction(data: MutableData?): Transaction.Result {
                    val stockRef = data?.child("stock")
                    val stock = (stockRef?.value ?: return Transaction.abort()) as Long

                    stockRef.value = stock + (if (consume) -1 else 1)
                    return Transaction.success(data)
                }

            })
        }

        user.dbRef.runTransaction(object : Transaction.Handler {
            override fun onComplete(err: DatabaseError?, committed: Boolean, snap: DataSnapshot?) {
                if (committed) Events.trigger(transactionEventID)
            }

            override fun doTransaction(data: MutableData?): Transaction.Result {
                val counterRef = data?.child("drinks/$drink")
                val drank = (counterRef?.value ?: 0) as Long

                counterRef?.value = drank + (if (consume) 1 else -1)
                return Transaction.success(data)
            }

        })
    }
}