package com.example.cloudhub.domain.use_cases

import com.example.cloudhub.common.Resource
import com.example.cloudhub.domain.model.LoginResult
import com.example.cloudhub.domain.repository.CloudRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: CloudRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<LoginResult>> = flow {
        try {
            emit(Resource.Loading<LoginResult>())
            val loginResult = repository.login(email, password).toLoginResult()
            emit(Resource.Success<LoginResult>(loginResult))
        } catch (e: HttpException) {
            emit(Resource.Error<LoginResult>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<LoginResult>("Cant reach server. Check your network connection"))
        }
    }
}