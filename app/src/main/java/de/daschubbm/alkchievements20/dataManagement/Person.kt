package de.daschubbm.alkchievements20.dataManagement

import com.google.firebase.database.*
import de.daschubbm.alkchievements20.control.Events

class Person(val dbRef: DatabaseReference, snapshot: DataSnapshot) {
    val name: String = dbRef.key
    val appVersion = snapshot.child("appVersion").value as String
    val drinks: MutableMap<Drink, Int> = mutableMapOf()
    val alkchievements: MutableMap<Alkchievement, Int> = mutableMapOf()

    init {

        /*********************************************************
         *              Load drinks and numbers drank            *
         *********************************************************/

        for (drink in snapshot.child("drinks").children) {
            val drinkObj = FirebaseManager.allDrinks[drink.key]
            drinkObj?.let { drinks.put(it, (drink.value as Long).toInt()) }
        }

        /*********************************************************
         *            Load alkchievements and levels             *
         *********************************************************/

        for (alkchievement in snapshot.child("achievements").children) {
            val alkchievementObj = AlkchievementsManager.allAlkchievements[alkchievement.key]
            alkchievementObj?.let { alkchievements.put(it, (alkchievement.value as Long).toInt()) }
        }

        /*********************************************************
         *     Setup automatic updates when database changes     *
         *********************************************************/

        dbRef.child("drinks").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError?) {
                println(error?.message)
            }

            override fun onChildMoved(child: DataSnapshot?, previousSibling: String?) = Unit

            override fun onChildChanged(child: DataSnapshot?, previousSibling: String?) {
                FirebaseManager.allDrinks[child?.key]?.let {
                    val drank = (child?.value as Long).toInt()

                    drinks.put(it, drank)
                    Events.trigger("Person-Drink-Changed", listOf(child.key, drank))
                }
            }

            override fun onChildAdded(child: DataSnapshot?, previousSibling: String?) {
                FirebaseManager.allDrinks[child?.key]?.let {
                    val drank = (child?.value as Long).toInt()

                    drinks.put(it, drank)
                    Events.trigger("Person-Drink-Added", listOf(child.key, drank))
                }
            }

            override fun onChildRemoved(child: DataSnapshot?) {
                FirebaseManager.allDrinks[child?.key]?.let {
                    drinks.remove(it)
                    Events.trigger("Person-Drink-Removed", listOf(child!!.key))
                }
            }

        })
    }
}