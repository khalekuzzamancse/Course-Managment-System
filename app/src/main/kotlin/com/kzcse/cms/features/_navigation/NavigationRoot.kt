package com.kzcse.cms.features._navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.kzcse.cms.features.course_list.presentation.CourseDetailsScreen
import com.kzcse.cms.features.course_list.presentation.CourseListScreen

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val viewModel = viewModel { NavigationViewModel() }
    var backPressCountOnHome = remember { 0 }
    val context = LocalContext.current
    val backStack = viewModel.backStack
    BackHandler {
        if (backStack.size == 1) {
            backPressCountOnHome++
        } else {
            backPressCountOnHome = 0//reset
        }
        if (backPressCountOnHome >= 2) {
            (context as? Activity)?.finish()
        }
    }
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {
            //for device back-button
            viewModel.onBack()
        },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                is Route.Home -> {
                    NavEntry(key) {
                        CourseListScreen(
                            onDetailsRequest = viewModel::goToDetails
                        )
                    }
                }

                is Route.Details -> {
                    NavEntry(key) {
                        CourseDetailsScreen(
                            onBack = viewModel::onBack,
                            id = key.id
                        )
                    }
                }
                else -> throw kotlin.RuntimeException("Invalid root")
            }
        }
    )
}