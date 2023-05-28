package com.example.cloudhub.data.remote.pbo

import com.google.gson.annotations.SerializedName

data class RegistrationRequestBody(
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String
)