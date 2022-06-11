package com.c22ps305team.saferoute.data

import com.google.gson.annotations.SerializedName

data class CoordinateResponse(

	@field:SerializedName("statistic")
	val statistic: List<StatisticItem?>? = null
)

data class CoordinatesItem(

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)

data class CrimeInfo(

	@field:SerializedName("Murder: First-degree")
	val murderFirstDegree: Int? = null,

	@field:SerializedName("Manslaughter: Involuntary")
	val manslaughterInvoluntary: Int? = null,

	@field:SerializedName("Aggravated Assault")
	val aggravatedAssault: Int? = null,

	@field:SerializedName("Wire Fraud")
	val wireFraud: Int? = null,

	@field:SerializedName("Burglary")
	val burglary: Int? = null,

	@field:SerializedName("Theft")
	val theft: Int? = null,

	@field:SerializedName("Computer Crime")
	val computerCrime: Int? = null,

	@field:SerializedName("Securities Fraud")
	val securitiesFraud: Int? = null,

	@field:SerializedName("Attempt")
	val attempt: Int? = null,

	@field:SerializedName("Robbery")
	val robbery: Int? = null,

	@field:SerializedName("Statutory Rape")
	val statutoryRape: Int? = null,

	@field:SerializedName("Drug Possession")
	val drugPossession: Int? = null,

	@field:SerializedName("Manslaughter: Voluntary")
	val manslaughterVoluntary: Int? = null,

	@field:SerializedName("Bribery")
	val bribery: Int? = null,

	@field:SerializedName("Harassment")
	val harassment: Int? = null,

	@field:SerializedName("Rape")
	val rape: Int? = null,

	@field:SerializedName("Hate Crimes")
	val hateCrimes: Int? = null,

	@field:SerializedName("Sexual Assault")
	val sexualAssault: Int? = null,

	@field:SerializedName("Open Container (of alcohol)")
	val openContainerOfAlcohol: Int? = null,

	@field:SerializedName("Arson")
	val arson: Int? = null,

	@field:SerializedName("Racketeering / RICO")
	val racketeeringRICO: Int? = null,

	@field:SerializedName("Murder: Second-degree")
	val murderSecondDegree: Int? = null,

	@field:SerializedName("Assault / Battery")
	val assaultBattery: Int? = null,

	@field:SerializedName("Shoplifting")
	val shoplifting: Int? = null,

	@field:SerializedName("Medical Marijuana")
	val medicalMarijuana: Int? = null,

	@field:SerializedName("Homicide")
	val homicide: Int? = null,

	@field:SerializedName("Kidnapping")
	val kidnapping: Int? = null,

	@field:SerializedName("Stalking")
	val stalking: Int? = null,

	@field:SerializedName("Conspiracy")
	val conspiracy: Int? = null,

	@field:SerializedName("Fraud")
	val fraud: Int? = null,

	@field:SerializedName("Disturbing the Peace")
	val disturbingThePeace: Int? = null,

	@field:SerializedName("Forgery")
	val forgery: Int? = null,

	@field:SerializedName("Aiding and Abetting / Accessory")
	val aidingAndAbettingAccessory: Int? = null,

	@field:SerializedName("Disorderly Conduct")
	val disorderlyConduct: Int? = null,

	@field:SerializedName("Child Abandonment")
	val childAbandonment: Int? = null,

	@field:SerializedName("Solicitation")
	val solicitation: Int? = null,

	@field:SerializedName("Pyramid Schemes")
	val pyramidSchemes: Int? = null,

	@field:SerializedName("Cyberbullying")
	val cyberbullying: Int? = null,

	@field:SerializedName("DUI / DWI")
	val dUIDWI: Int? = null,

	@field:SerializedName("Drug Manufacturing and Cultivation")
	val drugManufacturingAndCultivation: Int? = null,

	@field:SerializedName("Perjury")
	val perjury: Int? = null,

	@field:SerializedName("Insurance Fraud")
	val insuranceFraud: Int? = null,

	@field:SerializedName("Probation Violation")
	val probationViolation: Int? = null,

	@field:SerializedName("Criminal Contempt of Court")
	val criminalContemptOfCourt: Int? = null,

	@field:SerializedName("Public Intoxication")
	val publicIntoxication: Int? = null,

	@field:SerializedName("Indecent Exposure")
	val indecentExposure: Int? = null,

	@field:SerializedName("Child Pornography")
	val childPornography: Int? = null,

	@field:SerializedName("Domestic Violence")
	val domesticViolence: Int? = null,

	@field:SerializedName("Credit / Debit Card Fraud")
	val creditDebitCardFraud: Int? = null,

	@field:SerializedName("Identity Theft")
	val identityTheft: Int? = null,

	@field:SerializedName("Drug Trafficking / Distribution")
	val drugTraffickingDistribution: Int? = null,

	@field:SerializedName("Embezzlement")
	val embezzlement: Int? = null,

	@field:SerializedName("MIP: A Minor in Possession")
	val mIPAMinorInPossession: Int? = null,

	@field:SerializedName("White Collar Crimes")
	val whiteCollarCrimes: Int? = null,

	@field:SerializedName("Telemarketing Fraud")
	val telemarketingFraud: Int? = null,

	@field:SerializedName("Child Abuse")
	val childAbuse: Int? = null,

	@field:SerializedName("Vandalism")
	val vandalism: Int? = null,

	@field:SerializedName("Prostitution")
	val prostitution: Int? = null,

	@field:SerializedName("Tax Evasion / Fraud")
	val taxEvasionFraud: Int? = null,

	@field:SerializedName("Money Laundering")
	val moneyLaundering: Int? = null,

	@field:SerializedName("Extortion")
	val extortion: Int? = null
)

data class StatisticItem(

	@field:SerializedName("crime_info")
	val crimeInfo: CrimeInfo? = null,

	@field:SerializedName("total_crime")
	val totalCrime: Int? = null,

	@field:SerializedName("subdistrict")
	val subdistrict: String? = null,

	@field:SerializedName("coordinates")
	val coordinates: List<CoordinatesItem?>? = null
)
