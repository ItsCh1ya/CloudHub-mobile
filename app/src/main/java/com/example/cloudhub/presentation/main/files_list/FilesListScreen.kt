package com.example.cloudhub.presentation.main.files_list

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cloudhub.R
import com.example.cloudhub.presentation.components.TopBar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun FilesListScreen(
    viewModel: FilesListViewModel = hiltViewModel(), navController: NavController
) {
    val context = LocalContext.current
    val state = viewModel.filesResultState.value
    val launcher =
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
                        FileItem(filename, viewModel)
                    }
                }
            }
        }
    })
}

@Composable
private fun FileItem(
    filename: String,
    viewModel: FilesListViewModel
) {
    val showDownloadIcon = remember {
        mutableStateOf(true)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth() // Removed padding(horizontal = 16.dp)
                    .padding(
                        vertical = 8.dp,
                        horizontal = 8.dp
                    ),// Removed padding(horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f) // Added weight to expand text to fill available space
                        .padding(end = 16.dp) // Added padding to separate text and button,
                        .align(Alignment.CenterVertically),
                    text = filename,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                if (!viewModel.isFileDownloaded(filename) and showDownloadIcon.value) {
                    IconButton(
                        onClick = {
                            viewModel.downloadFile(filename)
                            showDownloadIcon.value = false
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_download_24),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}
