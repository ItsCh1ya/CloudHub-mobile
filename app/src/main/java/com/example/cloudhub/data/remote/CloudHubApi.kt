package com.example.cloudhub.data.remote

import com.example.cloudhub.data.remote.dto.FilesResultDto
import com.example.cloudhub.data.remote.dto.LoginResultDto
import com.example.cloudhub.data.remote.dto.RegistrationResultDto
import com.example.cloudhub.data.remote.dto.UploadFileDto
import com.example.cloudhub.data.remote.pbo.LoginRequestBody
import com.example.cloudhub.data.remote.pbo.RegistrationRequestBody
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming

interface CloudHubApi {
    @POST("api/login")
    suspend fun login(@Body loginRequestBody: LoginRequestBody): LoginResultDto

    @POST("api/register")
    suspend fun register(@Body registrationRequestBody: RegistrationRequestBody): RegistrationResultDto

    @GET("api/get_files")
    suspend fun getFiles(): FilesResultDto

    @Streaming
    @GET("api/receive_file/{fileId}")
    suspend fun downloadFile(@Path("fileId") fileId: String): ResponseBody

    @Multipart
    @POST("api/send_file")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): UploadFileDto
}