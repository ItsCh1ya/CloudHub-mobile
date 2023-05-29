package com.example.cloudhub.domain.use_cases

import com.example.cloudhub.domain.repository.CloudRepository
import okhttp3.ResponseBody
import javax.inject.Inject

class DownloadFileUseCase @Inject constructor(
    private val repository: CloudRepository
) {
    suspend operator fun invoke(fileId: String): ResponseBody {
        return repository.downloadFile(fileId)
    }
}