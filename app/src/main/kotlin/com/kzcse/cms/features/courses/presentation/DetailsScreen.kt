@file:Suppress("ComposableNaming")

package com.kzcse.cms.features.courses.presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kzcse.cms.core.ui.BackIcon
import com.kzcse.cms.core.ui.NoDataView
import com.kzcse.cms.core.ui.ScreenStrategy
import com.kzcse.cms.core.ui.SpacerHorizontal
import com.kzcse.cms.core.ui.VoidCallback
import com.kzcse.cms.features.courses.presentation.CourseDetailsViewModel
import com.kzcse.cms.features.courses.domain.CourseModel
import com.kzcse.cms.features.courses.domain.InstructorModel

@Composable
fun CourseDetailsScreen(
    modifier: Modifier = Modifier,
    id: String,
    viewModel: CourseDetailsViewModel= hiltViewModel(),
    onBack: VoidCallback
) {
    //val viewModel = viewModel { CourseDetailsViewModel() }
    val model = viewModel.course.collectAsState().value
    val isLoading=viewModel.isLoading.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.read(id)

    }
    ScreenStrategy(
        controller = viewModel,
        modifier = modifier,
        navigationIcon = {
            BackIcon(
                onClick = onBack
            )
        },
        title = {
            Text("Details Screen")
        }
    ){
        if (model!=null){
            _CourseDetailView(
                model = model,
                onEnrollClick = {
                    viewModel.enroll()
                }
            )
        }
        else{
            if(!isLoading) NoDataView()
        }

    }

}


@Composable
fun _CourseDetailView(
    modifier: Modifier = Modifier,
    model: CourseModel,
    onEnrollClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Title
        Text(
            text = model.name,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Instructor
        Text(
            text = "Instructor: ${model.instructor.name} [${model.instructor.expertiseLevel}]",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
            text = model.descriptionShort,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Meta info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "⭐ ${model.rating}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = if (model.isPremium) "$${model.priceUsd}" else "Free",
                style = MaterialTheme.typography.bodyMedium
            )
            SpacerHorizontal(16)
            Text(
                text = "Duration: ${model.durationWeeks} weeks",
                style = MaterialTheme.typography.bodyMedium
            )
        }



        Spacer(modifier = Modifier.height(16.dp))

        // Tags
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            model.tags.forEach { tag ->
                AssistChip(
                    onClick = {},
                    label = { Text(tag) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Enroll button
        Button(
            onClick = onEnrollClick,
           enabled = !model.isEnrolled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (true) "ENROLLED" else "MARK AS ENROLLED"
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val model = CourseModel(
        id = "KOTLIN-001",
        name = "Android App Development with Compose",
        descriptionShort = "Build modern Android apps from scratch using Kotlin and Jetpack Compose.",
        instructor = InstructorModel(
            name = "Prof. Anika",
            expertiseLevel = "Senior Developer"
        ),
        durationWeeks = 8,
        priceUsd = 49.99,
        isPremium = true,
        tags = listOf("Compose", "MVVM", "Coroutines"),
        rating = 4.8,
        isEnrolled = false
    )
    _CourseDetailView(
        model = model,
        onEnrollClick = {}
    )
}