package com.kzcse.cms
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kzcse.cms.core.data_src.local.RoomDbProvider
import com.kzcse.cms.features._navigation.NavigationRoot
import com.kzcse.cms.features.course_list.presentation.CourseListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        RoomDbProvider.init(this)
        setContent {
            CourseManagementSystemTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationRoot(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

