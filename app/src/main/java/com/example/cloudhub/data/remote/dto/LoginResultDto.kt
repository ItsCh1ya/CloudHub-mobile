package com.example.cloudhub.data.remote.dto

import com.example.cloudhub.domain.model.LoginResult

data class LoginResultDto(
    val success: Boolean,
    val message: String?
) {
    fun toLoginResult(): LoginResult {
        return LoginResult(success, message)
    }
}