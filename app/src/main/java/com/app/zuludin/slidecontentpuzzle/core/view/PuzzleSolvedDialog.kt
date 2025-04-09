package com.app.zuludin.slidecontentpuzzle.core.view

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import coil3.size.Size
import com.app.zuludin.slidecontentpuzzle.R

@Composable
fun PuzzleSolvedDialog(onPlayAgain: () -> Unit) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(AnimatedImageDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()


    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context).data(data = R.drawable.trophy).apply(block = {
                            size(
                                Size.ORIGINAL
                            )
                        }).build(), imageLoader = imageLoader
                    ),
                    contentDescription = null,
                    modifier = Modifier.width(120.dp)
                )
                Text(
                    "Puzzle is Solved",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xff5cb85c),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
                ElevatedButton(
                    onClick = { onPlayAgain() },
                    content = { Text("Play Again") },
                    modifier = Modifier.padding(top = 16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff5cb85c)),
                    contentPadding = PaddingValues(horizontal = 32.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PuzzleSolvedDialogPreview() {
    PuzzleSolvedDialog {}
}