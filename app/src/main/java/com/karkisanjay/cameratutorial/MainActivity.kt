package com.karkisanjay.cameratutorial

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.karkisanjay.cameratutorial.ui.theme.CameraTutorialTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var imageUri by remember { mutableStateOf<Uri?>(null) }
            val bitmapImage = remember { mutableStateOf<Bitmap?>(null) }

            val galleryLauncher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                    imageUri = uri
                    bitmapImage.value = null
                }
            val cameraLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.TakePicturePreview(),
                    onResult = {
                        bitmapImage.value = it
                        imageUri = null
                    })

            val camPermission =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        cameraLauncher.launch(null)
                    } else {
                        Toast.makeText(context, "Permission was not granted", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

            val storagePermission =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        galleryLauncher.launch("image/*")
                    } else {
                        Toast.makeText(context, "Permission was not granted", Toast.LENGTH_SHORT)
                            .show()
                    }
                }





            CameraTutorialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { it ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        contentAlignment = Alignment.TopCenter
                    ) {


                        if (bitmapImage.value != null || imageUri != null) {
                            if (imageUri != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(imageUri)
                                        .build(),
                                    contentDescription = "Camera Image",
                                    contentScale = ContentScale.Inside,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.8f)
                                )

                            }

                            if (bitmapImage.value != null) {
                                Image(
                                    bitmap = bitmapImage.value!!.asImageBitmap(),
                                    contentDescription = "Camera Image",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.8f)
                                )
                            }
                        } else {
                            Text(
                                text = "Nothing to display.",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            Button(onClick = {
                                camPermission.launch(android.Manifest.permission.CAMERA)
                            }) {
                                Text(text = "Open Camera")
                            }

                            Button(onClick = {
                                storagePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            }) {
                                Text(text = "Open Gallery")
                            }

                        }


                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CameraTutorialTheme {

    }
}