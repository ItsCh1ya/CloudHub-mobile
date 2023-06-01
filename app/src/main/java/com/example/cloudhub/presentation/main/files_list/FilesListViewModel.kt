package com.example.cloudhub.presentation.main.files_list

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudhub.common.Resource
import com.example.cloudhub.domain.model.FilesResult
import com.example.cloudhub.domain.use_cases.DownloadFileUseCase
import com.example.cloudhub.domain.use_cases.FetchFilesUseCase
import com.example.cloudhub.domain.use_cases.UploadFileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FilesListViewModel @Inject constructor(
    private val fetchFilesUseCase: FetchFilesUseCase,
    private val downloadFileUseCase: DownloadFileUseCase,
    private val uploadFileUseCase: UploadFileUseCase
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

    fun uploadFile(uri: Uri, context: Context) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)

            inputStream?.let {
                val fileName = getFileNameFromUri(contentResolver, uri)
                val file = File(context.cacheDir, fileName)
                file.createNewFile()

                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                inputStream.close() // Close the input stream

                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val response = uploadFileUseCase(body) // Use use-case instead
                Log.d("pickerDocs", response.toString())
                fetchFiles()
            }
        }
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

    fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String {
        var name: String = "myfile"
        val returnCursor = contentResolver.query(uri, null, null, null, null)
        returnCursor?.let { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            name = cursor.getString(nameIndex)
            cursor.close()
        }
        return name
    }

    fun isFileDownloaded(fileName: String): Boolean {
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDirectory, fileName)
        return file.exists()
    }
}