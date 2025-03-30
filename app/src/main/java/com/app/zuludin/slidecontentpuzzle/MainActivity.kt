package com.app.zuludin.slidecontentpuzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.app.zuludin.slidecontentpuzzle.core.theme.SlideContentPuzzleTheme
import com.app.zuludin.slidecontentpuzzle.navigation.SlidePuzzleNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SlideContentPuzzleTheme {
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    SlidePuzzleNavGraph(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController
                    )
                }
            }
        }
    }
}
