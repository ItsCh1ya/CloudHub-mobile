package com.example.cloudhub.domain.model

data class FilesResult(
    val files: List<String>,
    val success: Boolean,
    val isLoading: Boolean?
)