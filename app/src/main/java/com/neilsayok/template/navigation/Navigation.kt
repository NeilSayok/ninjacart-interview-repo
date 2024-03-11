package com.neilsayok.template.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neilsayok.template.commonviewmodels.PreferenceViewModel
import com.neilsayok.template.ui.launchScreen.FirstScreen


@Composable
fun Navigation(
    preferenceViewModel: PreferenceViewModel,
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()



    NavHost(
        navController = navController,
        startDestination = Screen.FirstScreen.route
    ) {

        animatedComposable(route = Screen.FirstScreen.route) {
            FirstScreen(navController = navController,
                preferenceViewModel = preferenceViewModel
            )
        }






    }

//    navController.addOnDestinationChangedListener { _, _, _ ->
//        checkMaintenanceFlag(navController = navController)
//    }







}

fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit, // Add NavBackStackEntry argument
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(500)
            )
        },
        content = content
    )

}

