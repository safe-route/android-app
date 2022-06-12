package com.c22ps305team.saferoute.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getCurrentDate(): String {
        val dateformat = SimpleDateFormat("yyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateformat.format(date)
    }
}