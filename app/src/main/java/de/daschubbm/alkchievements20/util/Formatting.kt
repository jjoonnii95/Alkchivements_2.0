package de.daschubbm.alkchievements20.util

import de.daschubbm.alkchievements20.dataManagement.Drink

fun Drink.formattedPrice(): String {
    val string = this.price.toString()

    if (string.length == 1) return "0,0" + string + "€"
    else if (string.length == 2) return "0," + string + "€"

    return string.substring(0, string.length - 2) + "," + string.substring(string.length - 2) + "€"
}