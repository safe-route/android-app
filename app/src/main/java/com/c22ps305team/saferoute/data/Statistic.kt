package com.c22ps305team.saferoute.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistic (
    var crime_info: MutableMap<String, Int>? = null,
    //var crime_info: List<CrimeInfo?>? = null,
    var subdistrict: String? = null,
    var total_crime: Int? = null
): Parcelable
