package com.example.cloudhub.domain.use_cases

import com.example.cloudhub.common.Resource
import com.example.cloudhub.domain.model.RegistrationResult
import com.example.cloudhub.domain.repository.CloudRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: CloudRepository
) {
    operator fun invoke(name: String, email: String, password: String): Flow<Resource<RegistrationResult>> = flow {
        try {
            emit(Resource.Loading<RegistrationResult>())
            val registrationResult = repository.register(name, password, email).toRegistrationResult()
            emit(Resource.Success<RegistrationResult>(registrationResult))
        } catch (e: HttpException) {
            emit(Resource.Error<RegistrationResult>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<RegistrationResult>("Cant reach server. Check your network connection"))
        }
    }
}