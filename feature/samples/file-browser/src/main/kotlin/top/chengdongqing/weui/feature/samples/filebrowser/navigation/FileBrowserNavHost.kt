package top.chengdongqing.weui.feature.samples.filebrowser.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import top.chengdongqing.weui.feature.samples.filebrowser.filelist.FileListScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun FileBrowserNavHost(
    navController: NavHostController,
    folders: SnapshotStateList<String>,
    rootPath: String
) {
    NavHost(
        navController,
        startDestination = "home/{path}",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable("home/{path}") { navBackStackEntry ->
            val path = navBackStackEntry.arguments?.getString("path")
                ?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
                ?: rootPath

            BackHandler(folders.size > 1) {
                folders.removeLastOrNull()
                navController.navigateUp()
            }

            FileListScreen(path) {
                val encodedPath = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                navController.navigate("home/$encodedPath")
                folders.add(it)
            }
        }
    }
}