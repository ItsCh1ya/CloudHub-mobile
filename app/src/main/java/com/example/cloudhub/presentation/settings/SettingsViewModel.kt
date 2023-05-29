package com.example.cloudhub.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.cloudhub.common.delCookie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    fun logout(context: Context, navController: NavController){
        delCookie(context)
        navController.navigate("auth") {
            popUpTo("main") {
                inclusive = true
            }
        }
    }
}