package com.example.cloudhub.domain.use_cases

import com.example.cloudhub.common.Resource
import com.example.cloudhub.domain.model.FilesResult
import com.example.cloudhub.domain.repository.CloudRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FetchFilesUseCase @Inject constructor(
    private val repository: CloudRepository
) {
    operator fun invoke(): Flow<Resource<FilesResult>> = flow {
        try {
            emit(Resource.Loading<FilesResult>())
            val filesResult = repository.getFiles().toFilesResult()
            emit(Resource.Success<FilesResult>(filesResult))
        } catch (e: HttpException) {
            emit(Resource.Error<FilesResult>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<FilesResult>("Cant reach server. Check your network connection"))
        }
    }
}