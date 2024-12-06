package id.adiyusuf.qrscannerwithlibrary

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.adiyusuf.qrscannerwithlibrary.ui.theme.QRScannerWithLibraryTheme
import kotlinx.coroutines.launch
import qrscanner.CameraLens
import qrscanner.QrScanner

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRScannerWithLibraryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Content(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    var qrCodeURL by remember { mutableStateOf("") }
    var flashlightOn by remember { mutableStateOf(false) }
    var openImagePicker by remember { mutableStateOf(value = false) }
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFF1D1C22))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            QrScanner(
                modifier = Modifier
                    .clipToBounds()
                    .clip(shape = RoundedCornerShape(size = 14.dp)),
                flashlightOn = flashlightOn,
                openImagePicker = openImagePicker,
                onCompletion = {
                    qrCodeURL = it
                },
                imagePickerHandler = {
                    openImagePicker = it
                },
                onFailure = {
                    coroutineScope.launch {
                        if (it.isEmpty()) {
                            Toast.makeText(context, "Invalid qr code", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                cameraLens = CameraLens.Back
            )


        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(
                    Alignment.BottomCenter
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .background(
                        color = Color(0xFFF9F9F9),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .height(35.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = if (flashlightOn) Icons.Filled.Close else Icons.Filled.Check,
                        "flash",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                flashlightOn = !flashlightOn
                            })

                    VerticalDivider(
                        modifier = Modifier,
                        thickness = 1.dp,
                        color = Color(0xFFD8D8D8)
                    )

                    Image(
                        imageVector = Icons.Rounded.AddCircle,
                        contentDescription = "gallery",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                openImagePicker = true
                            }
                    )
                }
            }

            if (qrCodeURL.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .padding(bottom = 22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = qrCodeURL,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f),
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )

                    Icon(
                        Icons.Filled.Add,
                        "CopyAll",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                clipboardManager.setText(AnnotatedString((qrCodeURL)))
                            },
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    QRScannerWithLibraryTheme {
        Content()
    }
}