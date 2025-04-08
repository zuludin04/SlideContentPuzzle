package com.app.zuludin.slidecontentpuzzle.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.zuludin.slidecontentpuzzle.R

@Composable
fun HomeScreen(onOpenNumberPuzzle: () -> Unit, onOpenImagePuzzle: () -> Unit) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF8de9d5),
                            Color(0xFF32c4c0)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Icon(
                        painterResource(R.drawable.ic_swipe),
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                    Text("Slide Puzzle", color = Color.White)
                }
                Box(modifier = Modifier.height(56.dp))
                GameMenuButton("Number", R.drawable.ic_pin, onOpenNumberPuzzle)
                Box(modifier = Modifier.height(16.dp))
                GameMenuButton("Image", R.drawable.ic_picture, onOpenImagePuzzle)
            }
        }
    }
}

@Composable
private fun GameMenuButton(label: String, icon: Int, onOpenPuzzle: () -> Unit) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(48.dp)
            .border(2.dp, Color.White, RoundedCornerShape(10.dp))
            .clickable { onOpenPuzzle() }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(painterResource(icon), null, tint = Color.White)
            Box(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 18.sp, color = Color.White)
        }
    }
}

@Preview
@Composable
fun GameMenuButtonPreview() {
    GameMenuButton("Number", R.drawable.ic_pin) {}
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onOpenImagePuzzle = {}, onOpenNumberPuzzle = {})
}