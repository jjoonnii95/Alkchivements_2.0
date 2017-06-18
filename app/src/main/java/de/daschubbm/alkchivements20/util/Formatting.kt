package de.daschubbm.alkchivements20.util

fun Short.asPrice(): String {
    val string = this.toString()

    if (string.length == 1) return "0,0" + string + "€"
    else if (string.length == 2) return "0," + string + "€"

    return string.substring(0, string.length - 2) + "," + string.substring(string.length - 2) + "€"
}