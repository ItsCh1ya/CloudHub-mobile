package com.example.cloudhub.data.remote

import com.example.cloudhub.data.remote.dto.FilesResultDto
import com.example.cloudhub.data.remote.dto.LoginResultDto
import com.example.cloudhub.data.remote.dto.RegistrationResultDto
import com.example.cloudhub.data.remote.pbo.LoginRequestBody
import com.example.cloudhub.data.remote.pbo.RegistrationRequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CloudHubApi {
    @POST("/api/login")
    suspend fun login(@Body loginRequestBody: LoginRequestBody): LoginResultDto

    @POST("api/register")
    suspend fun register(@Body registrationRequestBody: RegistrationRequestBody): RegistrationResultDto

    @GET("/api/get_files")
    suspend fun getFiles(): FilesResultDto

    //TODO: download and upload files
}