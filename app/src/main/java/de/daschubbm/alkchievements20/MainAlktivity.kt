package de.daschubbm.alkchievements20

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import de.daschubbm.alkchievements20.mainAlktivity.DrinksAlkdapter
import kotlinx.android.synthetic.main.alktivity_main.*

class MainAlktivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alktivity_main)

        val user = ItemUser(R.drawable.background, "Maxl")
        val categories = mutableListOf<ItemCategory>()

        val drinks = mutableListOf<ItemDrink>()

        drinks.add(ItemDrink("Almdudler", 80,R.drawable.almdudler))
        drinks.add(ItemDrink("Radler", 180,R.drawable.radler))
        drinks.add(ItemDrink("Wasser", 30,R.drawable.wasser))

        categories.add(ItemCategory("Alkoholfrei", drinks))
        categories.add(ItemCategory("Wenig Alkohol", drinks))

        drinksView.layoutManager = LinearLayoutManager(this)
        drinksView.adapter = DrinksAlkdapter(categories, user)
    }
}
