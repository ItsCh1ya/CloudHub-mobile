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
        loginUseCase(email, password).onEach {
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
                    _logState.value = LoginResult(false, message = it.message)
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    _logState.value = LoginResult(false, message = "Loading, please wait...")
                }
            }
        }.launchIn(viewModelScope)
    }
}