package com.kzcse.cms.features._navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.kzcse.cms.core.data_src.local.RoomDbProvider
import com.kzcse.cms.features._core.MonitorConnectivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        RoomDbProvider.init(this)
        MonitorConnectivity.start(this)
        setContent {
            CourseManagementSystemTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    NavigationRoot(modifier = Modifier.Companion.padding(innerPadding))
                }
            }
        }
    }
}