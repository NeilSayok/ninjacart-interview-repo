package com.neilsayok.template.navigation

sealed class Screen(val route : String) {

//    object Screen : Screen("_screen")
    object HomeScreen : Screen("home_screen")
    object SelectQuantityScreen : Screen("select_quantity_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun Screen.getFireBaseLoadTraceName() =
        "${ this.route.replace("_(.)".toRegex()) { it.groupValues[1].uppercase() } }-LoadTrace"
}