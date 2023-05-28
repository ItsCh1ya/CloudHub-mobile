package com.example.cloudhub.data.remote.dto

import com.example.cloudhub.domain.model.RegistrationResult

data class RegistrationResultDto(
    val success: Boolean,
    val message: String?
) {
    fun toRegistrationResult(): RegistrationResult {
        return RegistrationResult(success, message)
    }
}