package com.example.cloudhub.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Text
import com.example.cloudhub.common.isCookieExist
import com.example.cloudhub.presentation.auth.login.LoginScreen
import com.example.cloudhub.presentation.auth.register.RegScreen
import com.example.cloudhub.presentation.ui.theme.CloudHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CloudHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val loginedIn = isCookieExist(LocalContext.current)
                    NavHost(
                        navController = navController,
                        startDestination = if (loginedIn) "main" else "auth"
                    ) {
                        composable("settings") { }
                        navigation(startDestination = "login", route = "auth") {
                            composable("login") { LoginScreen(navController = navController) }
                            composable("register") { RegScreen(navController = navController) }
                        }
                        navigation(startDestination = "files_list", route = "main") {
                            composable("files_list") {
                                Text(text = "MAIN")
                            }
                        }
                    }
                }
            }
        }
    }
}