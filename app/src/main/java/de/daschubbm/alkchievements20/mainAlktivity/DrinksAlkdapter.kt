package de.daschubbm.alkchievements20.mainAlktivity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.daschubbm.alkchievements20.R
import de.daschubbm.alkchievements20.dataManagement.Drink
import de.daschubbm.alkchievements20.dataManagement.DrinkCategory
import de.daschubbm.alkchievements20.dataManagement.FirebaseManager
import de.daschubbm.alkchievements20.dataManagement.Person
import de.daschubbm.alkchievements20.util.formattedPrice
import kotlinx.android.synthetic.main.drink_category_item.view.*
import kotlinx.android.synthetic.main.drink_header_item.view.*
import kotlinx.android.synthetic.main.drink_item.view.*

const val PRICE_PREFIX = "\ud83d\udcb0 "
const val DRANK_PREFIX = "\ud83d\udc44 "
const val INVENTORY_PREFIX = "\ud83d\uddc4 "

const val MOST_EXPENSIVE_PREFIX = "\ud83d\udcb8 "
const val LAST_PREFIX = "\ud83d\udd53 "
const val TOP_PREFIX = "\ud83d\udd1d "

class DrinksAlkdapter(categories: Collection<DrinkCategory>, val user: Person) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val HEADER_ITEM = 0
    val CATEGORY_ITEM = 1
    val DRINK_ITEM = 2

    val items = mutableListOf<Any>()

    var blockActions = false

    val onClick: (View) -> Unit = { v ->
        if (!blockActions) {
            val tag = v.name.text

            blockActions = true
            FirebaseManager.consumeDrink(user, tag as String, { blockActions = false })
        }
    }

    val onLongClick: (View) -> Boolean = { v ->
        if (!blockActions) {
            val tag = v.name.text

            blockActions = true
            FirebaseManager.returnDrink(user, tag as String, { blockActions = false })
        }

        true //Long click has been accepted
    }

    init {
        items.add(user)
        for (cat in categories) {
            items.add(cat)
            for (drink in cat.drinks) {
                items.add(drink)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is DrinkHolder) holder.bind(getDrink(position), user)
        else if (holder is CategoryHolder) holder.bind(getCategory(position))
        else if (holder is HeadHolder) holder.bind(user)
    }

    private fun getCategory(position: Int): DrinkCategory {
        return items[position] as DrinkCategory
    }

    private fun getDrink(position: Int): Drink {
        return items[position] as Drink
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> return HeadHolder(LayoutInflater.from(parent!!.context)
                    .inflate(R.layout.drink_header_item, parent, false))
            1 -> return CategoryHolder(LayoutInflater.from(parent!!.context)
                    .inflate(R.layout.drink_category_item, parent, false))
            else -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.drink_item, parent, false)
                view.setOnClickListener(onClick)
                view.setOnLongClickListener(onLongClick)

                return DrinkHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] is Drink) return DRINK_ITEM
        if (items[position] is DrinkCategory) return CATEGORY_ITEM

        return HEADER_ITEM
    }
}

class DrinkHolder(view: View) : RecyclerView.ViewHolder(view) {
    val preview = view.preview
    val name = view.name
    val price = view.price
    val drank = view.drank
    val inventory = view.inventory

    fun bind(drink: Drink, user: Person) {
        preview.setImageResource(R.drawable.radler)
        name.text = drink.name
        price.text = PRICE_PREFIX + drink.formattedPrice()
        drank.text = DRANK_PREFIX + user.drinks.getOrDefault(drink, 0)
        inventory.text = INVENTORY_PREFIX + drink.stock
    }
}

class CategoryHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = view.title

    fun bind(category: DrinkCategory) {
        title.text = category.name
    }
}

class HeadHolder(view: View) : RecyclerView.ViewHolder(view) {
    val username = view.username
    val picture = view.picture
    val mostExpensive = view.mostExpensive
    val last = view.last
    val top = view.most

    fun bind(user: Person) {
        username.text = user.name

        picture.setImageResource(R.drawable.wasser)
        mostExpensive.text = MOST_EXPENSIVE_PREFIX + "Goaßmaß"
        last.text = LAST_PREFIX + "Schnops"
        top.text = TOP_PREFIX + "Radla"
    }
}
