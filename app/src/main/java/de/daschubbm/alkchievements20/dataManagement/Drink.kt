package de.daschubbm.alkchievements20.dataManagement

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import de.daschubbm.alkchievements20.control.Events

class Drink(val dbRef: DatabaseReference, snapshot: DataSnapshot) {
    val name: String = dbRef.key
    var price: Short = (snapshot.child("price").value as Long).toShort()
    var stock: Int = (snapshot.child("stock").value as Long).toInt()

    init {
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snap: DataSnapshot?) {
                price = (snap?.child("price")?.value as Long).toShort()
                stock = (snap.child("stock")?.value as Long).toInt()

                Events.trigger("Drink-Changed", listOf(this@Drink))
            }

            override fun onCancelled(error: DatabaseError?) {
                println(error?.message)
            }
        })
    }
}