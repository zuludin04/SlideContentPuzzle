package com.app.zuludin.slidecontentpuzzle.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.zuludin.slidecontentpuzzle.ui.HomeScreen
import com.app.zuludin.slidecontentpuzzle.ui.PuzzlePhotoScreen
import com.app.zuludin.slidecontentpuzzle.ui.PuzzleScreen

@Composable
fun SlidePuzzleNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController,
        startDestination = SlidePuzzleNavigation.HOME_SCREEN,
        modifier = modifier
    ) {
        composable(SlidePuzzleNavigation.HOME_SCREEN) {
            HomeScreen(
                onOpenNumberPuzzle = { navController.navigate(SlidePuzzleNavigation.PUZZLE_SCREEN) },
                onOpenImagePuzzle = { navController.navigate(SlidePuzzleNavigation.PUZZLE_PHOTO_SCREEN) }
            )
        }
        composable(SlidePuzzleNavigation.PUZZLE_SCREEN) { PuzzleScreen() }
        composable(SlidePuzzleNavigation.PUZZLE_PHOTO_SCREEN) { PuzzlePhotoScreen() }
    }
}