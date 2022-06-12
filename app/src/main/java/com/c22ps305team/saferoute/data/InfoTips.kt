package com.c22ps305team.saferoute.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoTips (
    var tittle: String = "",
    var photo: String = "",
    var content: String = "",
    var source: String = ""
): Parcelable


