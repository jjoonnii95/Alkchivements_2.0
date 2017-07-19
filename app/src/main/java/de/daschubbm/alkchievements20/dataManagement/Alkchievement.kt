package de.daschubbm.alkchievements20.dataManagement

import java.util.regex.Pattern

data class Alkchievement(val identifier: String, val name: String, val description: String) {
    val levels: MutableList<Int>

    init {
        levels = mutableListOf()

        val matcher = Pattern.compile("%(([1-9][0-9]*[ ]*,[ ]*)*[1-9][0-9]*)%").matcher(description)

        if (matcher.find()) {
            val substring = matcher.group(1)
            val levelStrings = substring.split(Regex(" *, *"))
            levelStrings.mapTo(levels) { it.toInt() }
        } else {
            levels.add(1)
        }
    }
}