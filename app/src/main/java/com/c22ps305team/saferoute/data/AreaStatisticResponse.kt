package com.c22ps305team.saferoute.data

import com.google.gson.annotations.SerializedName

data class AreaStatisticResponse(

    @field:SerializedName("crime_info")
    val crimeInfo: CrimeInfos? = null,

    @field:SerializedName("total_crime")
    val totalCrime: Int? = null,

    @field:SerializedName("subdistrict")
    val subdistrict: String? = null,

    val listCrime: List<CrimeInfos>? = null
)

data class CrimeInfos(

    @field:SerializedName("Conspiracy")
    val conspiracy: Int? = null,

    @field:SerializedName("Murder: First-degree")
    val murderFirstDegree: Int? = null,

    @field:SerializedName("Manslaughter: Involuntary")
    val manslaughterInvoluntary: Int? = null,

    @field:SerializedName("Aggravated Assault")
    val aggravatedAssault: Int? = null,

    @field:SerializedName("Fraud")
    val fraud: Int? = null,

    @field:SerializedName("Disturbing the Peace")
    val disturbingThePeace: Int? = null,

    @field:SerializedName("Theft")
    val theft: Int? = null,

    @field:SerializedName("Securities Fraud")
    val securitiesFraud: Int? = null,

    @field:SerializedName("Attempt")
    val attempt: Int? = null,

    @field:SerializedName("Robbery")
    val robbery: Int? = null,

    @field:SerializedName("Statutory Rape")
    val statutoryRape: Int? = null,

    @field:SerializedName("Manslaughter: Voluntary")
    val manslaughterVoluntary: Int? = null,

    @field:SerializedName("Forgery")
    val forgery: Int? = null,

    @field:SerializedName("Harassment")
    val harassment: Int? = null,

    @field:SerializedName("Rape")
    val rape: Int? = null,

    @field:SerializedName("Sexual Assault")
    val sexualAssault: Int? = null,

    @field:SerializedName("Aiding and Abetting / Accessory")
    val aidingAndAbettingAccessory: Int? = null,

    @field:SerializedName("Open Container (of alcohol)")
    val openContainerOfAlcohol: Int? = null,

    @field:SerializedName("Arson")
    val arson: Int? = null,

    @field:SerializedName("Disorderly Conduct")
    val disorderlyConduct: Int? = null,

    @field:SerializedName("Murder: Second-degree")
    val murderSecondDegree: Int? = null,

    @field:SerializedName("Assault / Battery")
    val assaultBattery: Int? = null,

    @field:SerializedName("Child Abandonment")
    val childAbandonment: Int? = null,

    @field:SerializedName("Solicitation")
    val solicitation: Int? = null,

    @field:SerializedName("Pyramid Schemes")
    val pyramidSchemes: Int? = null,

    @field:SerializedName("Homicide")
    val homicide: Int? = null,

    @field:SerializedName("Kidnapping")
    val kidnapping: Int? = null,

    @field:SerializedName("Cyberbullying")
    val cyberbullying: Int? = null,

    @field:SerializedName("Stalking")
    val stalking: Int? = null
)
