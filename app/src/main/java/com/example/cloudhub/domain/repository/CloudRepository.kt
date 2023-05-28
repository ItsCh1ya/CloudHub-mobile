package com.example.cloudhub.domain.repository

import com.example.cloudhub.data.remote.dto.FilesResultDto
import com.example.cloudhub.data.remote.dto.LoginResultDto
import com.example.cloudhub.data.remote.dto.RegistrationResultDto

interface CloudRepository {

    suspend fun login(email: String, password: String): LoginResultDto

    suspend fun register(name: String, password: String, email: String): RegistrationResultDto

    suspend fun getFiles(): FilesResultDto
}