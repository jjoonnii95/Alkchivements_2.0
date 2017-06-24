package de.daschubbm.alkchievements20.util

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

fun readJson(context: Context, resId: Int): JSONObject {
    val reader = BufferedReader(InputStreamReader(context.resources.openRawResource(resId)))
    var rawJSON = ""

    var line: String? = reader.readLine()
    while (line != null) {
        rawJSON += line + "\n"
        line = reader.readLine()
    }

    reader.close()

    return JSONObject(rawJSON)
}