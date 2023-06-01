package com.example.cloudhub.presentation.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cloudhub.R
import com.example.cloudhub.common.delCookie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, navController: NavController) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(text = title)
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_logout_24),
                contentDescription = "logout",
                Modifier
                    .clickable {
                        logout(context = context, navController = navController)
                    }
                    .padding(8.dp)
            )
        }
    )
}

fun logout(context: Context, navController: NavController){
    delCookie(context)
    navController.navigate("auth") {
        popUpTo("main") {
            inclusive = true
        }
    }
}