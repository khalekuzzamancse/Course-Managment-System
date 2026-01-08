package com.kzcse.cms.features._navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kzcse.cms.features._navigation.Route.Home.route
import kotlinx.serialization.Serializable

sealed interface Route {
    val route: String

    @Serializable
    data object Home : NavKey, Route {
        override val route = "Home"
    }

    @Serializable
    data class Details(val id: String) : NavKey, Route {
        override val route = "Details"
    }

}

class NavigationViewModel() : ViewModel() {
    val backStack: NavBackStack = mutableStateListOf(Route.Home)

    fun pop() {
        backStack.removeAt(backStack.lastIndex)
        backStack.lastOrNull()
    }

     fun goToDetails(id: String) {
        backStack.add(Route.Details(id))
    }

    fun onBack() {
        try {
            backStack.removeAt(backStack.lastIndex)
        }
        catch (_: Throwable){}
    }

}