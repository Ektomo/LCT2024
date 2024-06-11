package ivan.gorbunov.lct2024.ui.screens.core

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun TakePhoto(
    onPhotoTaken: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var hasPermission by remember { mutableStateOf(false) }

    RequestCameraPermission {
        hasPermission = true
    }

    if (hasPermission) {
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                photoUri?.let {
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    onPhotoTaken(bitmap)
                }
            }
        }

        val photoFile = remember {
            File.createTempFile(
                "photo_", ".jpg",
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ).apply {
                photoUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", this)
            }
        }

        LaunchedEffect(Unit) {
            photoUri?.let {
                launcher.launch(it)
            }
        }
    }
}