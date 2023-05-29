package com.example.cloudhub.presentation.auth.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun MyOutlinedTextField(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    state: MutableState<String>,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = onValueChange,
        shape = CircleShape,
        label = {
            Text(text = label)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier,
        singleLine = true,
        isError = isError,
        visualTransformation = visualTransformation
    )
}