package com.example.cloudhub.presentation.auth.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.cloudhub.common.Resource
import com.example.cloudhub.domain.model.LoginResult
import com.example.cloudhub.domain.use_cases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _logState = mutableStateOf(LoginResult(false, ""))
    val logState: State<LoginResult> = _logState
    fun login(email: String, password: String, navController: NavController, context: Context) {
        // Check if the email and password are valid
        if (!isEmailValid(email)) {
            showError(context, "Email doesn't looks like valid")
            return
        }

        if (!isPasswordValid(password)) {
            showError(context, "Min length of password - 4 symbols")
            return
        }

        // Perform the loginUseCase
        loginUseCase(email, password).onEach { it ->
            when (it) {
                is Resource.Success -> {
                    _logState.value = LoginResult(true, "Account successfully registered")
                    navController.navigate("main") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                }
                is Resource.Error -> {
                    it.message?.let { it1 -> showError(context, it1) }
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    showError(context,  "Loading, please wait...")
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
        return email.matches(emailRegex)
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 4
    }
    private fun showError(context: Context, message: String){
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}