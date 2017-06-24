package de.daschubbm.alkchievements20.dataManagement

data class Person(val name: String, val appVersion: Int, val drinks: Map<Drink, Int>, val alkchievements: Map<Alkchievement, Int>, val iconImgRes: String)