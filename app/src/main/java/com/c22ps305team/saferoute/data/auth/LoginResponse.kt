package com.c22ps305team.saferoute.data.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("token")
	val token: String
)
