package com.c22ps305team.saferoute.utils

import android.content.Context
import java.io.IOException

fun readJsonFile(context: Context, fileName: String): String? {

    val jsonString: String

    try {
        jsonString = context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
    return jsonString
}