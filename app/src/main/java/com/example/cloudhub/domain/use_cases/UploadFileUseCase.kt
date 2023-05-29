package com.example.cloudhub.domain.use_cases

import com.example.cloudhub.data.remote.dto.UploadFileDto
import com.example.cloudhub.domain.repository.CloudRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val repository: CloudRepository
) {
    suspend operator fun invoke(file: MultipartBody.Part): UploadFileDto {
        return repository.uploadFile(file)
    }
}