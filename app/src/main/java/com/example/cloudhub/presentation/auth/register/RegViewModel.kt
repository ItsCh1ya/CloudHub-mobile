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
    fun register(name: String, password: String, email: String, navController: NavController, context: Context) {
        registerUseCase(name, email, password).onEach {
            when (it) {
                is Resource.Success -> {
                    _regState.value = RegistrationResult(true, "Account successfully registered")
                    navController.navigate("main")
                }
                is Resource.Error -> {
                    _regState.value = RegistrationResult(false, message = it.message)
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    _regState.value = RegistrationResult(false, message = "Loading, please wait...")
                }
            }
        }.launchIn(viewModelScope)
    }
}