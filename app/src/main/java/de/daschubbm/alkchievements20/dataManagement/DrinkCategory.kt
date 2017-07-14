package de.daschubbm.alkchievements20.dataManagement

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class DrinkCategory(val dbRef: DatabaseReference, snapshot: DataSnapshot) {
    var name: String = snapshot.key
    val drinks: MutableSet<Drink> = mutableSetOf()

    init {
        /*********************************************************
         *                     Sync drinks                       *
         *********************************************************/

        for (drink in snapshot.children) {
            FirebaseManager.allDrinks[drink.key]?.let { drinks.add(it) }
        }

        /*********************************************************
         *      Setup automatic updates on database change       *
         *********************************************************/

        dbRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError?) {
                println(error?.message)
            }

            override fun onChildMoved(child: DataSnapshot?, previousSibling: String?) = Unit

            override fun onChildChanged(child: DataSnapshot?, previousSibling: String?) = Unit

            override fun onChildAdded(child: DataSnapshot?, previousSibling: String?) {
                FirebaseManager.allDrinks[child?.key]?.let { drinks.add(it) }
            }

            override fun onChildRemoved(child: DataSnapshot?) {
                FirebaseManager.allDrinks[child?.key]?.let { drinks.remove(it) }
            }
        })
    }
}