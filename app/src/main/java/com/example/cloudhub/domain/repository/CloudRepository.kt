package com.example.cloudhub.domain.repository

import com.example.cloudhub.data.remote.dto.FilesResultDto
import com.example.cloudhub.data.remote.dto.LoginResultDto
import com.example.cloudhub.data.remote.dto.RegistrationResultDto
import com.example.cloudhub.data.remote.dto.UploadFileDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface CloudRepository {

    suspend fun login(email: String, password: String): LoginResultDto

    suspend fun register(name: String, password: String, email: String): RegistrationResultDto

    suspend fun getFiles(): FilesResultDto
    suspend fun downloadFile(fileId: String): ResponseBody
    suspend fun uploadFile(file: MultipartBody.Part): UploadFileDto
}