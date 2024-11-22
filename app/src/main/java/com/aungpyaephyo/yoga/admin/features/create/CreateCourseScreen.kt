@file:OptIn(ExperimentalMaterial3Api::class)

package com.aungpyaephyo.yoga.admin.features.create




import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aungpyaephyo.yoga.admin.R
import com.aungpyaephyo.yoga.admin.data.model.DifficultyLevel
import com.aungpyaephyo.yoga.admin.data.model.YogaClassType
import com.aungpyaephyo.yoga.admin.data.model.YogaEventType

import com.aungpyaephyo.yoga.admin.data.repo.YogaRepository
import com.aungpyaephyo.yoga.admin.theme.UniversalYogaTheme

import com.aungpyaephyo.yoga.admin.utils.LocationHelper

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable
import java.time.format.TextStyle
import java.util.Locale

@Serializable
data class CreateCourseRoute(val courseId: String? = null)

fun NavController.navigateToCreateCourse(courseId: String? = null) {
    navigate(CreateCourseRoute(courseId))
}

fun NavGraphBuilder.createCourseScreen(
    repo: YogaRepository,
    locationHelper: LocationHelper,
    onBack: () -> Unit,
) {
    composable<CreateCourseRoute> {
        val route = it.toRoute<CreateCourseRoute>()

        val viewModel: CreateCourseScreenViewModel = viewModel(
            factory = CreateCourseScreenViewModel.Factory(
                courseId = route.courseId,
                repo = repo,
            //    imageUtils = imageUtils,
                locationHelper = locationHelper
            )
        )

        val locationPermissionRequestLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    viewModel.useCurrentLocation()
                }
                permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    viewModel.useCurrentLocation()
                }
            }
        }

        LaunchedEffect(viewModel.navigateToHome.value) {
            if (viewModel.navigateToHome.value) {
                onBack()
            }
        }

        Screen(
            courseId = route.courseId,
            inputError = viewModel.inputError.value,
            onBack = onBack,
            onSave = viewModel::onSave,
            selectedDayOfWeek = viewModel.selectedDayOfWeek.value,
            onDayOfWeekSelected = { viewModel.selectedDayOfWeek.value = it },
            duration = viewModel.duration.value,
            onDurationChange = {
                viewModel.duration.value = it
                viewModel.resetInputError()
            },
            time = viewModel.time.value,
            onTimeChange = {
                viewModel.time.value = it
                viewModel.resetInputError()
            },
            capacity = viewModel.capacity.value,
            onCapacityChange = {
                viewModel.capacity.value = it
                viewModel.resetInputError()
            },
            price = viewModel.price.value,
            onPriceChange = {
                viewModel.price.value = it
                viewModel.resetInputError()
            },
            description = viewModel.description.value,
            onDescriptionChange = { viewModel.description.value = it },
            classType = viewModel.classType.value,
            onClassTypeChange = { viewModel.classType.value = it },
            difficulty = viewModel.difficulty.value,
            onDifficultyChange = { viewModel.difficulty.value = it },

            onDismissSave = { viewModel.showConfirmSave = false },
            showConfirmSave = viewModel.showConfirmSave,
            onConfirmSave = viewModel::create,
            onLaunchCamera = {

            },
            onLaunchPhotoPicker = {

            },
            selectedEventType = viewModel.eventType,
            onSelectedEventTypeChange = { eventType ->
                viewModel.updateEventType(eventType)

            },
            onRemoveImage = {},
            latitude = viewModel.latitude,
            onLatitudeChange = { viewModel.latitude = it },
            longitude = viewModel.longitude,
            onLongitudeChange = { viewModel.longitude = it },
            onUseCurrentLocation = {
                locationPermissionRequestLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            },
            url = viewModel.url,
            onUrlChange = { viewModel.url = it }
        )
    }
}

@Composable
private fun Screen(
    courseId: String?,
    showConfirmSave: Boolean,
    onDismissSave: () -> Unit,
    inputError: CourseInputError?,
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
    onBack: () -> Unit,
    onSave: () -> Unit,
    onConfirmSave: () -> Unit,
    onLaunchCamera: () -> Unit,
    onLaunchPhotoPicker: () -> Unit,
    onRemoveImage: (String) -> Unit,
    selectedEventType: YogaEventType,
    onSelectedEventTypeChange: (YogaEventType) -> Unit,
    latitude: TextFieldValue,
    onLatitudeChange: (TextFieldValue) -> Unit,
    longitude: TextFieldValue,
    onLongitudeChange: (TextFieldValue) -> Unit,
    onUseCurrentLocation: () -> Unit,
    url: TextFieldValue,
    onUrlChange: (TextFieldValue) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    if (showConfirmSave) {
        Dialog(onDismissRequest = { onDismissSave() }) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        color = Color.Black,
                        text = "Confirm Course Details",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        color = Color.Black,
                        text = "Please check the preview details below and confirm.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.size(16.dp))
                    ConfirmationInfo(
                        label = "Day: ",
                        value = selectedDayOfWeek.getDisplayName(
                            TextStyle.FULL_STANDALONE,
                            Locale.getDefault()
                        )
                    )
                    ConfirmationInfo(
                        label = "Start Time: ",
                        value = time.text
                    )
                    ConfirmationInfo(
                        label = "Duration: ",
                        value = "${duration.text} min"
                    )
                    Spacer(Modifier.size(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.size(8.dp))
                    ConfirmationInfo(
                        label = "Class Type: ",
                        value = classType.displayName
                    )
                    ConfirmationInfo(
                        label = "Difficulty: ",
                        value = difficulty.displayName
                    )
//
                    ConfirmationInfo(
                        label = "Description: ",
                        value = description.text.ifBlank { "No description." }
                    )
                    Spacer(Modifier.size(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.size(8.dp))
                    ConfirmationInfo(
                        label = "Capacity: ",
                        value = "${capacity.text} persons"
                    )
                    ConfirmationInfo(
                        label = "Price Per Class: ",
                        value = "£${price.text}"
                    )
//                    ConfirmationInfo(
//                        label = "Cancellation Policy: ",
//                        value = cancellationPolicy.displayName
//                    )
                    Spacer(Modifier.size(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.size(8.dp))
//                    ConfirmationInfo(
//                        label = "Images: ",
//                        value = if (images.isNotEmpty()) {
//                            "${images.size} images"
//                        } else {
//                            "No images"
//                        }
//                    )
                    ConfirmationInfo(
                        label = "Location: ",
                        value = selectedEventType.displayName
                    )

                    Spacer(Modifier.size(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton (onClick = onDismissSave) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            onDismissSave()
                            onConfirmSave()
                        }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

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
                    Text(courseId?.let { "Edit Yoga Course" } ?: "Create Yoga Course")
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
        val listState = rememberLazyListState()

        // scroll to the section that has input error
        LaunchedEffect(inputError) {
            when (inputError) {
                CourseInputError.StartTime,
                CourseInputError.Duration -> {
                    listState.animateScrollToItem(0)
                }

                CourseInputError.Capacity,
                CourseInputError.Price -> {
                    listState.animateScrollToItem(2)
                }

                null -> {}
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier,
                state = listState,
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
            ) {
                item {
                    Column {
                        Spacer(Modifier.size(8.dp))
//
                        ScheduleSection(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            selectedDayOfWeek = selectedDayOfWeek,
                            onDayOfWeekSelected = onDayOfWeekSelected,
                            duration = duration,
                            onDurationChange = onDurationChange,
                            time = time,
                            onTimeChange = onTimeChange,
                            inputError = inputError,
                        )
                        Spacer(Modifier.size(16.dp))
                    }
                }

                item {
                    Column {

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
//                            targetAudience = targetAudience,
//                            onTargetAudienceChange = onTargetAudienceChange
                        )
                        Spacer(Modifier.size(16.dp))
                    }
                }

                item {
                    Column {

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
//                            cancellationPolicy = cancellationPolicy,
//                            onCancellationPolicyChange = onCancellationPolicyChange,
                            inputError = inputError
                        )
                        Spacer(Modifier.size(16.dp))
                    }
                }


                item {
                    Spacer(
                        Modifier
                            .imePadding()
                            .size(100.dp)
                    )
                }
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
                        text = courseId?.let { "Update Yoga" } ?: "Create Yoga",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmationInfo(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Text(
        color = Color.Black,
        modifier = modifier,
        text = getAnnotatedString(
            label = label,
            value = value
        ),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun getAnnotatedString(
    label: String,
    value: String
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append(label)
        }
        append(value)
    }
}


@Composable
private fun OnlineLocationOption(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelected: () -> Unit,
    url: TextFieldValue,
    onUrlChange: (TextFieldValue) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        RadioOption(
            label = "Online Course",
            selected = selected,
            onSelected = onSelected
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = selected,
            value = url,
            onValueChange = onUrlChange,
            label = { Text("Class Url") },
        )
    }
}

@Composable
private fun InPersonLocationOption(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelected: () -> Unit,
    latitude: TextFieldValue,
    onLatitudeChange: (TextFieldValue) -> Unit,
    longitude: TextFieldValue,
    onLongitudeChange: (TextFieldValue) -> Unit,
    onUseCurrentLocation: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        RadioOption(
            label = "In-Person Course",
            selected = selected,
            onSelected = onSelected
        )
     //   Spacer(modifier = Modifier.size(8.dp))
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            OutlinedTextField(
//                modifier = Modifier
//                    .weight(1f),
//                enabled = selected,
//                value = latitude,
//                onValueChange = onLatitudeChange,
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                label = { Text("Latitude") },
//            )
//            OutlinedTextField(
//                modifier = Modifier
//                    .weight(1f),
//                enabled = selected,
//                value = longitude,
//                onValueChange = onLongitudeChange,
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                label = { Text("Longitude") },
//            )
//        }
//        Spacer(Modifier.size(8.dp))
        OutlinedButton (
            enabled = selected,
            onClick = onUseCurrentLocation
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_location),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text("Use Current Location")
        }
    }
}

@Composable
private fun RadioOption(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onSelected: () -> Unit,
) {
    Row(
        modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null // null recommended for accessibility with screenreaders
        )
        Text(
            text = label, color = Color.Black,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
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
    inputError: CourseInputError?,
) {
    val isError = when (inputError) {
        CourseInputError.StartTime,
        CourseInputError.Duration -> true

        else -> false
    }
    OutlinedCard (
        modifier = modifier,
       // border = if (isError) BorderStroke(1.dp, MaterialTheme.colorScheme.error) else null
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            SectionSubtitle(
                title = "Schedule"
            )
            Spacer(Modifier.size(4.dp))
            DayOfWeekSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedDayOfWeek = selectedDayOfWeek,
                onDayOfWeekSelected = onDayOfWeekSelected
            )

            Spacer(Modifier.size(8.dp))
            CourseTimePicker(
                modifier = Modifier.fillMaxWidth(),
                value = time,
                onTimeSelected = onTimeChange,
                isError = inputError == CourseInputError.StartTime
            )
            Spacer(Modifier.size(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = duration,
                onValueChange = onDurationChange,
                suffix = { Text("mins") },
                supportingText = { Text("Required*") },
                singleLine = true,
                maxLines = 1,
                label = { Text("Duration") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = inputError == CourseInputError.Duration
            )

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
//    targetAudience: TargetAudience,
//    onTargetAudienceChange: (TargetAudience) -> Unit,
)
{
    OutlinedCard(modifier = modifier) {
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
            HorizontalDivider()
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
            HorizontalDivider()
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
    inputError: CourseInputError?,
) {
    val isError = when (inputError) {
        CourseInputError.Capacity,
        CourseInputError.Price -> true

        else -> false
    }
    OutlinedCard(
        modifier = modifier,
      //  border = if (isError) BorderStroke(1.dp, MaterialTheme.colorScheme.error) else null
    ) {

        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            SectionSubtitle(

                title = "Pricing & Capability"

            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = capacity,
                onValueChange = onCapacityChange,
                suffix = { Text("person") },
                label = { Text("Max Capacity") },
                supportingText = { Text("Required*") },
                singleLine = true,
                maxLines = 1,
                isError = inputError == CourseInputError.Capacity,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = price,
                onValueChange = onPriceChange,
                suffix = { Text("£") },
                label = { Text("Price") },
                singleLine = true,
                maxLines = 1,
                supportingText = { Text("Required*") },
                isError = inputError == CourseInputError.Price,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(Modifier.size(12.dp))
            HorizontalDivider()
            Spacer(Modifier.size(12.dp))

//
        }
    }
}

//@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScheduleSectionPreview() {
    UniversalYogaTheme {
        ScheduleSection(
            selectedDayOfWeek = DayOfWeek.MONDAY,
            onDayOfWeekSelected = {},
            duration = TextFieldValue(),
            onDurationChange = {},
            time = TextFieldValue(),
            onTimeChange = {},
            inputError = null
        )
    }
}

//@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailsSectionPreview() {
    UniversalYogaTheme {
        DetailsSection(
            description = TextFieldValue(),
            onDescriptionChange = {},
            classType = YogaClassType.HARMONY_FAMILY_YOGA,
            onClassTypeChange = {},
            difficulty = DifficultyLevel.BEGINNER,
            onDifficultyChange = {},
        )
    }
}

//@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PricingSectionPreview() {
    UniversalYogaTheme {
        PricingSection(
            capacity = TextFieldValue(),
            onCapacityChange = {},
            price = TextFieldValue(),
            onPriceChange = {},
//            cancellationPolicy = CancellationPolicy.STRICT,
//            onCancellationPolicyChange = {},
            inputError = null
        )
    }
}