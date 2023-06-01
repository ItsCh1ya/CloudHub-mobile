package com.example.cloudhub.presentation.auth.register

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.cloudhub.common.Resource
import com.example.cloudhub.domain.model.RegistrationResult
import com.example.cloudhub.domain.use_cases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _regState = mutableStateOf(RegistrationResult(false, ""))
    val regState: State<RegistrationResult> = _regState
    fun register(
        name: String,
        password: String,
        email: String,
        navController: NavController,
        context: Context
    ) {
        // Check if the name, email, and password are valid
        if (!isNameValid(name)) {
            showError(context, "Invalid name")
            return
        }

        if (!isEmailValid(email)) {
            showError(context, "Email doesn't looks like valid")
            return
        }

        if (!isPasswordValid(password)) {
            showError(context, "Min length of password - 4 symbols")
            return
        }

        // Perform the registerUseCase
        registerUseCase(name, email, password).onEach { it ->
            when (it) {
                is Resource.Success -> {
                    _regState.value = RegistrationResult(true, "Account successfully registered")
                    navController.navigate("main")
                }

                is Resource.Error -> {
                    _regState.value = RegistrationResult(false, message = it.message)
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                    _regState.value = RegistrationResult(false, message = "Loading, please wait...")
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun isNameValid(name: String): Boolean {
        val nameRegex = Regex("^[a-zA-Z0-9]{2,}$")
        return name.matches(nameRegex)
    }

    private fun isPasswordValid(password: String): Boolean { // TODO: repeating code
        return password.isNotEmpty() && password.length >= 4
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
        return email.matches(emailRegex)
    }

    private fun showError(context: Context, message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}