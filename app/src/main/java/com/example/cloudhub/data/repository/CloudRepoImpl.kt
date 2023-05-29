package com.example.cloudhub.data.repository

import com.example.cloudhub.data.remote.CloudHubApi
import com.example.cloudhub.data.remote.dto.FilesResultDto
import com.example.cloudhub.data.remote.dto.LoginResultDto
import com.example.cloudhub.data.remote.dto.RegistrationResultDto
import com.example.cloudhub.data.remote.dto.UploadFileDto
import com.example.cloudhub.data.remote.pbo.LoginRequestBody
import com.example.cloudhub.data.remote.pbo.RegistrationRequestBody
import com.example.cloudhub.domain.repository.CloudRepository
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import javax.inject.Inject

class CloudRepoImpl @Inject constructor(
    private val api: CloudHubApi
) : CloudRepository {
    override suspend fun login(email: String, password: String): LoginResultDto {
        return api.login(
            LoginRequestBody(
                email, password
            )
        )
    }

    override suspend fun register(
        name: String,
        password: String,
        email: String
    ): RegistrationResultDto {
        return api.register(
            RegistrationRequestBody(
                name, password, email
            )
        )
    }

    override suspend fun getFiles(): FilesResultDto {
        return api.getFiles()
    }

    override suspend fun downloadFile(fileId: String): ResponseBody {
        return api.downloadFile(fileId)
    }

    override suspend fun uploadFile(file: MultipartBody.Part): UploadFileDto {
        return api.uploadFile(file)
    }
}