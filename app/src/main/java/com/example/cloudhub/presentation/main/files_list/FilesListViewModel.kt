package com.example.cloudhub.presentation.main.files_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudhub.common.Resource
import com.example.cloudhub.domain.model.FilesResult
import com.example.cloudhub.domain.use_cases.FetchFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FilesListViewModel @Inject constructor(
    private val fetchFilesUseCase: FetchFilesUseCase
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
}