@file:OptIn(ExperimentalMaterial3Api::class)

package com.myolwinoo.universalyoga.admin.features.create

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.CancellationPolicy
import com.myolwinoo.universalyoga.admin.data.model.DayOfWeek
import com.myolwinoo.universalyoga.admin.data.model.DifficultyLevel
import com.myolwinoo.universalyoga.admin.data.model.TargetAudience
import com.myolwinoo.universalyoga.admin.data.model.YogaClassType
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import kotlinx.serialization.Serializable

@Serializable
data class CreateCourseRoute(val courseId: String? = null)

fun NavController.navigateToCreateCourse(courseId: String? = null) {
    navigate(CreateCourseRoute(courseId))
}

fun NavGraphBuilder.createCourseScreen(
    repo: YogaRepository,
    onBack: () -> Unit,
) {

    composable<CreateCourseRoute> {
        val route = it.toRoute<CreateCourseRoute>()
        val viewModel: CreateCourseScreenViewModel = viewModel(
            factory = CreateCourseScreenViewModel.Factory(
                repo = repo,
                courseId = route.courseId
            )
        )

        LaunchedEffect(viewModel.navigateToHome.value) {
            if (viewModel.navigateToHome.value) {
                onBack()
            }
        }

        Screen(
            courseId = route.courseId,
            onBack = onBack,
            onSave = viewModel::create,
            selectedDayOfWeek = viewModel.selectedDayOfWeek.value,
            onDayOfWeekSelected = { viewModel.selectedDayOfWeek.value = it },
            duration = viewModel.duration.value,
            onDurationChange = { viewModel.duration.value = it },
            time = viewModel.time.value,
            onTimeChange = { viewModel.time.value = it },
            capacity = viewModel.capacity.value,
            onCapacityChange = { viewModel.capacity.value = it },
            price = viewModel.price.value,
            onPriceChange = { viewModel.price.value = it },
            description = viewModel.description.value,
            onDescriptionChange = { viewModel.description.value = it },
            classType = viewModel.classType.value,
            onClassTypeChange = { viewModel.classType.value = it },
            difficulty = viewModel.difficulty.value,
            onDifficultyChange = { viewModel.difficulty.value = it },
            targetAudience = viewModel.targetAudience.value,
            onTargetAudienceChange = { viewModel.targetAudience.value = it },
            cancellationPolicy = viewModel.cancellationPolicy.value,
            onCancellationPolicyChange = { viewModel.cancellationPolicy.value = it }
        )
    }
}

@Composable
private fun Screen(
    courseId: String?,
    selectedDayOfWeek: DayOfWeek,
    onDayOfWeekSelected: (DayOfWeek) -> Unit,
    duration: TextFieldValue,
    onDurationChange: (TextFieldValue) -> Unit,
    time: TextFieldValue,
    onTimeChange: (TextFieldValue) -> Unit,
    capacity: TextFieldValue,
    onCapacityChange: (TextFieldValue) -> Unit,
    price: TextFieldValue,
    onPriceChange: (TextFieldValue) -> Unit,
    description: TextFieldValue,
    onDescriptionChange: (TextFieldValue) -> Unit,
    classType: YogaClassType,
    onClassTypeChange: (YogaClassType) -> Unit,
    difficulty: DifficultyLevel,
    onDifficultyChange: (DifficultyLevel) -> Unit,
    targetAudience: TargetAudience,
    onTargetAudienceChange: (TargetAudience) -> Unit,
    cancellationPolicy: CancellationPolicy,
    onCancellationPolicyChange: (CancellationPolicy) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f)
                ),
                title = {
                    Text(courseId?.let { "Edit Course" } ?: "Create Course")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "back button"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box {
            Column(
                modifier = Modifier
                    .padding(
                        top = 0.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.padding(top = innerPadding.calculateTopPadding()))
                SectionTitle(
                    title = "Schedule"
                )
                Spacer(Modifier.size(8.dp))
                ScheduleSection(
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                        .fillMaxWidth(),
                    selectedDayOfWeek = selectedDayOfWeek,
                    onDayOfWeekSelected = onDayOfWeekSelected,
                    duration = duration,
                    onDurationChange = onDurationChange,
                    time = time,
                    onTimeChange = onTimeChange
                )
                Spacer(Modifier.size(16.dp))

                SectionTitle(
                    title = "Class Details"
                )
                Spacer(Modifier.size(12.dp))
                DetailsSection(
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                        .fillMaxWidth(),
                    description = description,
                    onDescriptionChange = onDescriptionChange,
                    classType = classType,
                    onClassTypeChange = onClassTypeChange,
                    difficulty = difficulty,
                    onDifficultyChange = onDifficultyChange,
                    targetAudience = targetAudience,
                    onTargetAudienceChange = onTargetAudienceChange
                )
                Spacer(Modifier.size(16.dp))

                SectionTitle(
                    title = "Pricing & Policy"
                )
                Spacer(Modifier.size(12.dp))
                PricingSection(
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                        .fillMaxWidth(),
                    capacity = capacity,
                    onCapacityChange = onCapacityChange,
                    price = price,
                    onPriceChange = onPriceChange,
                    cancellationPolicy = cancellationPolicy,
                    onCancellationPolicyChange = onCancellationPolicyChange
                )
                Spacer(
                    Modifier
                        .imePadding()
                        .size(100.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f))
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 12.dp,
                        bottom = innerPadding.calculateBottomPadding() + 12.dp
                    )
                    .imePadding()
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onSave
                ) {
                    Text(
                        text = courseId?.let { "Update" } ?: "Create",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleSection(
    modifier: Modifier = Modifier,
    selectedDayOfWeek: DayOfWeek,
    onDayOfWeekSelected: (DayOfWeek) -> Unit,
    duration: TextFieldValue,
    onDurationChange: (TextFieldValue) -> Unit,
    time: TextFieldValue,
    onTimeChange: (TextFieldValue) -> Unit,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            SectionSubtitle(
                title = "Day"
            )
            Spacer(Modifier.size(4.dp))
            DayOfWeekSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedDayOfWeek = selectedDayOfWeek,
                onDayOfWeekSelected = onDayOfWeekSelected
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CourseTimePicker(
                    modifier = Modifier.weight(1f),
                    value = time,
                    onTimeSelected = onTimeChange
                )

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = duration,
                    onValueChange = onDurationChange,
                    suffix = {
                        Text("min")
                    },
                    maxLines = 1,
                    label = { Text("Duration") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

@Composable
fun DetailsSection(
    modifier: Modifier = Modifier,
    description: TextFieldValue,
    onDescriptionChange: (TextFieldValue) -> Unit,
    classType: YogaClassType,
    onClassTypeChange: (YogaClassType) -> Unit,
    difficulty: DifficultyLevel,
    onDifficultyChange: (DifficultyLevel) -> Unit,
    targetAudience: TargetAudience,
    onTargetAudienceChange: (TargetAudience) -> Unit,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            SectionSubtitle(
                title = "Class Types"
            )
            Spacer(Modifier.size(4.dp))
            ClassTypeSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedType = classType,
                onTypeSelected = onClassTypeChange
            )
            Spacer(Modifier.size(8.dp))

            SectionSubtitle(
                title = "Difficulty"
            )
            Spacer(Modifier.size(4.dp))
            DifficultySelector(
                modifier = Modifier.fillMaxWidth(),
                selectedValue = difficulty,
                onDifficultySelected = onDifficultyChange
            )
            Spacer(Modifier.size(8.dp))

            SectionSubtitle(
                title = "Target Audience"
            )
            Spacer(Modifier.size(4.dp))
            TargetAudienceSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedValue = targetAudience,
                onAudienceSelected = onTargetAudienceChange
            )
            Spacer(Modifier.size(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Description") },
                minLines = 4
            )
        }
    }
}

@Composable
fun PricingSection(
    modifier: Modifier = Modifier,
    capacity: TextFieldValue,
    onCapacityChange: (TextFieldValue) -> Unit,
    price: TextFieldValue,
    onPriceChange: (TextFieldValue) -> Unit,
    cancellationPolicy: CancellationPolicy,
    onCancellationPolicyChange: (CancellationPolicy) -> Unit,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = capacity,
                    onValueChange = onCapacityChange,
                    suffix = { Text("person") },
                    label = { Text("Max Capacity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = price,
                    onValueChange = onPriceChange,
                    suffix = { Text("£") },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
            Spacer(Modifier.size(12.dp))
            SectionSubtitle(
                title = "Cancellation Policy"
            )
            Spacer(Modifier.size(4.dp))
            CancellationPolicySelector(
                modifier = Modifier.fillMaxWidth(),
                selectedValue = cancellationPolicy,
                onPolicySelected = onCancellationPolicyChange
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScheduleSectionPreview() {
    UniversalYogaTheme {
        ScheduleSection(
            selectedDayOfWeek = DayOfWeek.MONDAY,
            onDayOfWeekSelected = {},
            duration = TextFieldValue(),
            onDurationChange = {},
            time = TextFieldValue(),
            onTimeChange = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailsSectionPreview() {
    UniversalYogaTheme {
        DetailsSection(
            description = TextFieldValue(),
            onDescriptionChange = {},
            classType = YogaClassType.FLOW_YOGA,
            onClassTypeChange = {},
            difficulty = DifficultyLevel.BEGINNER,
            onDifficultyChange = {},
            targetAudience = TargetAudience.BEGINNERS,
            onTargetAudienceChange = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PricingSectionPreview() {
    UniversalYogaTheme {
        PricingSection(
            capacity = TextFieldValue(),
            onCapacityChange = {},
            price = TextFieldValue(),
            onPriceChange = {},
            cancellationPolicy = CancellationPolicy.STRICT,
            onCancellationPolicyChange = {}
        )
    }
}
