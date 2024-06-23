package ivan.gorbunov.lct2024.ui.screens.client.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.applyCanvas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun ShareProfileScreen(
    name: String,
    surname: String,
    title: String,
    imageRes: Int ,
    onDismiss: () -> Unit// Ресурс изображения
) {
    val context = LocalContext.current
    val imageBitmap = remember { loadBitmapFromResource(context, imageRes) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            bitmap.value = createProfileImage(name, surname, title, imageBitmap)
        }
    }

    Dialog(onDismissRequest = onDismiss) {


        Column(
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))

                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "")
                }
            }
            bitmap.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
//                        .height(500.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .padding(8.dp)
                )
            }
            Button(onClick = {
                bitmap.value?.let { bmp ->
                    val imageFile = saveBitmapToFile(bmp, context.cacheDir, "profile_image.png")
                    val imageUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        imageFile
                    )
                    shareProfile(context, imageUri)
                }
            }) {
                Text("Поделиться")
            }
        }
    }
}

fun loadBitmapFromResource(context: Context, resId: Int): Bitmap {
    return BitmapFactory.decodeResource(context.resources, resId)
}

fun createProfileImage(name: String, surname: String, title: String, imageBitmap: Bitmap): Bitmap {
    val width = 800
    val height = 1500
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    bitmap.applyCanvas {
        drawColor(Color.WHITE)

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 60f
            textAlign = Paint.Align.CENTER
        }

        drawText("$name $surname", width / 2f, 200f, paint)
        drawText(title, width / 2f, 300f, paint)

        val imagePaint = Paint()
        val imageRect = Rect(100, 400, width - 100, 1400)
        drawBitmap(imageBitmap, null, imageRect, imagePaint)
    }

    return bitmap
}

fun saveBitmapToFile(bitmap: Bitmap, directory: File, fileName: String): File {
    val file = File(directory, fileName)
    try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}


fun shareProfile(context: Context, imageUri: Uri) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, imageUri)
        type = "image/png"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Поделиться профилем"))
}