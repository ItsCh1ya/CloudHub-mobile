package com.example.cloudhub.presentation.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cloudhub.presentation.auth.components.MyOutlinedTextField
import com.example.cloudhub.presentation.components.TopBar

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Column {
        TopBar("Authorization", navController, showLogout = false)

        Column(modifier = Modifier
            .padding(horizontal = 48.dp)
            .padding(top = 160.dp)) {
            Text(text = "Login", style = MaterialTheme.typography.headlineLarge)
            MyOutlinedTextField(
                state = email,
                label = "Email",
                keyboardType = KeyboardType.Email,
                onValueChange = { email.value = it }
            )
            MyOutlinedTextField(
                state = password,
                label = "Password",
                onValueChange = { password.value = it },
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 18.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(MaterialTheme.shapes.large)
                        .clickable { navController.navigate("register") }) {
                    Text(
                        text = "Register",
                        Modifier
                            .padding(8.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                Button(onClick = {
                    viewModel.login(email.value, password.value, navController, context)
                }) {
                    Text(text = "Login")
                }
            }
        }
    }
}