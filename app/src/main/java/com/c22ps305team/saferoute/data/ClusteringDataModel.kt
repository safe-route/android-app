package com.c22ps305team.saferoute.data

import com.google.gson.annotations.SerializedName

data class ClusteringDataModel(
	@field:SerializedName("centroids")
	val centroids: List<CentroidsItem?>? = null
)

data class CentroidsItem(

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("range")
	val range: Double? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)
