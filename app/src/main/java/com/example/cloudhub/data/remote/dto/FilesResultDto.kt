package com.example.cloudhub.data.remote.dto

import com.example.cloudhub.domain.model.FilesResult

data class FilesResultDto(
    val files: List<Any>,
    val success: Boolean
) {
    fun toFilesResult(): FilesResult {
        return FilesResult(files, success)
    }
}