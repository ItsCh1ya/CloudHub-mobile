package com.example.cloudhub.presentation.main.files_list

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cloudhub.presentation.components.TopBar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun FilesListScreen(
    viewModel: FilesListViewModel = hiltViewModel(), navController: NavController
) {
    val context = LocalContext.current
    val state = viewModel.filesResultState.value
    val launcher = // TODO: move logic to viewmodel, main thread overload
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val pickedImageUri1: Uri?
            val data = result.data
            Log.d("pickerDocs", data?.data.toString())
            pickedImageUri1 = data?.data

            pickedImageUri1?.let { uri ->
                viewModel.uploadFile(uri, context)
            }
        }


    Scaffold(topBar = {
        TopBar(title = "CloudHub", navController = navController)
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            val intent = Intent(
                Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
            launcher.launch(intent)
        }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Upload")
        }
    }, content = { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (state.success and state.files.isEmpty()) {
                Text(
                    text = "Your storage is empty.\n Tap on Floating Action Button, to upload files",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else if (state.isLoading == true) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Loading, please wait...")
                    }
                }
            } else if ((!state.success) and (state.isLoading == false)) {
                Text(
                    text = "An unknown error occurred. Please try again later.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else {
                LazyColumn {
                    items(state.files) { filename ->
                        Text(text = filename,
                            Modifier.clickable { viewModel.downloadFile(filename) })
                    }
                }
            }
        }
    })
}
