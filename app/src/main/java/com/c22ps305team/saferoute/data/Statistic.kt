package com.c22ps305team.saferoute.data


data class Statistic (
    var crimeInfo: List<CrimeInfoItems>? = null,
    var subdistrict: String? = null,
    var totalCrime: Int? = null
)

data class CrimeInfoItems (
    val aggravatedAssault: Int? = null,

    val aidingandAbettingAccessory: Int? = null,

    val arson: Int? = null,

    val assaultorBattery: Int? = null,

    /*Burglary

    Criminal Contempt of Court

    Drug Possession

    Forgery

    Harassment

    Homicide

    Kidnapping

    Manslaughter: Involuntary

    Medical Marijuana

    Murder: Second-degree

    Pyramid Schemes

    Rape

    Robbery

    Sexual Assault

    Stalking

    Statutory Rape

    Tax Evasion / Fraud

    Theft

    White Collar Crimes
    */
)