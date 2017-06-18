package de.daschubbm.alkchivements20

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import de.daschubbm.alkchivements20.mainAlktivity.Category
import de.daschubbm.alkchivements20.mainAlktivity.Drink
import de.daschubbm.alkchivements20.mainAlktivity.DrinksAlkdapter
import de.daschubbm.alkchivements20.mainAlktivity.User
import kotlinx.android.synthetic.main.alktivity_main.*

class MainAlktivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alktivity_main)

        val user = User(R.drawable.background, "Maxl")
        val categories = mutableListOf<Category>()

        val drinks = mutableListOf<Drink>()

        drinks.add(Drink("Almdudler", 80,R.drawable.almdudler))
        drinks.add(Drink("Radler", 180,R.drawable.radler))
        drinks.add(Drink("Wasser", 30,R.drawable.wasser))

        categories.add(Category("Alkoholfrei", drinks))
        categories.add(Category("Wenig Alkohol", drinks))

        drinksView.layoutManager = LinearLayoutManager(this)
        drinksView.adapter = DrinksAlkdapter(categories, user)
    }
}
