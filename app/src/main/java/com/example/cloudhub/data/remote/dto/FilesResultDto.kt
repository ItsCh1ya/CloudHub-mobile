package com.example.cloudhub.data.remote.dto

import com.example.cloudhub.domain.model.FilesResult

data class FilesResultDto(
    val files: List<String>,
    val success: Boolean
) {
    fun toFilesResult(): FilesResult {
        return FilesResult(files = files, success = success, isLoading = false)
    }
}