@file:Suppress("ComposableNaming")

package com.kzcse.cms.features.course_list.presentation

import android.R.attr.text
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kzcse.cms.core.ui.DividerHorizontal
import com.kzcse.cms.core.ui.LoadingView
import com.kzcse.cms.core.ui.NoDataView
import com.kzcse.cms.core.ui.ScreenStrategy
import com.kzcse.cms.core.ui.SearchBarView
import com.kzcse.cms.core.ui.SpacerHorizontal
import com.kzcse.cms.features.course_list.domain.CourseModel
import com.kzcse.cms.features.course_list.domain.InstructorModel

@Composable
fun CourseListScreen(
    modifier: Modifier = Modifier,
    onDetailsRequest: (String) -> Unit
) {
    val viewmodel = viewModel { CourseListViewModel() }
    val isLoading = viewmodel.isLoading.collectAsState().value
    val courses = viewmodel.courses.collectAsState().value
    LaunchedEffect(Unit) {
        viewmodel.read()
    }

    ScreenStrategy(
        controller = viewmodel,
        title = {
            Text("Courses")
        }
    ) {
        Column(
            modifier = modifier
        ) {
            SearchBarView(
                modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
                onSearch = {viewmodel.search(it ?: "") },
                searchHint = "Search..."
            )
            if (courses.isEmpty() && !isLoading) {
                NoDataView()
            } else {
                CourseList(
                    courses = courses,
                    onCourseClick = onDetailsRequest
                )
            }
        }
    }


}

@Composable
fun CourseList(
    modifier: Modifier = Modifier,
    courses: List<CourseModel>,
    onCourseClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(courses) { index, model ->
            _CourseView(
                model = model,
                onClick = { onCourseClick(model.id) }
            )
            if (index != courses.lastIndex)
                DividerHorizontal()
        }
    }

}

@Composable
private fun _CourseView(
    model: CourseModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = model.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "⭐ ${model.rating}",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = model.instructor.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.weight(1f))
                Text(
                    modifier = Modifier,
                    text = if (model.isPremium) {
                        "$${model.priceUsd}"
                    } else {
                        "Free"
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }


            Spacer(modifier = Modifier.height(4.dp))

            // Short description
            Text(
                text = model.descriptionShort,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Price + Enrollment status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tags: ${model.tags.joinToString(", ")}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.weight(1f))
                if (model.isEnrolled) {
                    Text(
                        text = "ENROLLED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


//@Composable
//fun StatusIcon(status: PriorityModel) {
//    val primary = MaterialTheme.colorScheme.primary
//    val color = primary.copy(alpha = 0.6f)
//
//    Box(
//        modifier = Modifier
//            .size(40.dp)
//            .background(color, RoundedCornerShape(8.dp)),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = when (status) {
//                PriorityModel.LOW -> "L"
//                PriorityModel.MEDIUM -> "M"
//                PriorityModel.HIGH -> "H"
//            },
//            color = Color.White,
//            fontSize = 20.sp
//        )
//    }
//}
//
@Preview(

)
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
    CourseList(
        courses = listOf(model, model, model),
        onCourseClick = {}
    )

}