package com.example.cloudhub.presentation.main.files_list

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudhub.common.Resource
import com.example.cloudhub.domain.model.FilesResult
import com.example.cloudhub.domain.use_cases.DownloadFileUseCase
import com.example.cloudhub.domain.use_cases.FetchFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class FilesListViewModel @Inject constructor(
    private val fetchFilesUseCase: FetchFilesUseCase,
    private val downloadFileUseCase: DownloadFileUseCase
) : ViewModel() {

    private val filesResultMutableState = mutableStateOf(FilesResult(
        files = listOf(),
        success = false,
        isLoading = true
    ))
    val filesResultState: State<FilesResult> = filesResultMutableState

    init {
        fetchFiles()
    }

    private fun fetchFiles() {
        fetchFilesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    filesResultMutableState.value = FilesResult(
                        files = result.data?.files ?: listOf(),
                        success = true,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    filesResultMutableState.value = FilesResult(files = listOf(), success = false, isLoading = false)
                }
                is Resource.Loading -> {
                    filesResultMutableState.value = FilesResult(files = listOf(), success = false, isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun downloadFile(filename: String){
        viewModelScope.launch {
            try {
                val responseBody = downloadFileUseCase(filename)
                saveFile(responseBody, filename)
            } catch (e: OutOfMemoryError) {
                e.localizedMessage?.let { Log.e("saveFile", it) }
            }
        }
    }

    private suspend fun saveFile(body: ResponseBody, filename: String): String = withContext(
        Dispatchers.IO) {
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDirectory, filename)

        try {
            body.byteStream().use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return@withContext ""
        } catch (e: Exception) {
            Log.e("saveFile", e.toString())
            return@withContext ""
        }
    }
}