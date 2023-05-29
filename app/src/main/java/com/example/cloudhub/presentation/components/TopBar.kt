package com.example.cloudhub.presentation.components

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, navController: NavController) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        actions = {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "settings",
                Modifier.clickable {
                    navController.navigate("settings")
                }.padding(8.dp)
            )
        }
    )
}