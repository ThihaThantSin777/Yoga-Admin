@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.aungpyaephyo.yoga.admin.data.model.YogaClass
import com.aungpyaephyo.yoga.admin.data.repo.YogaRepository
import com.aungpyaephyo.yoga.admin.features.yogaclass.create.ClassInputError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import java.util.UUID

private const val DEBOUNCE_MILLIS = 300L

class CreateYogaClassViewModel(
    private val classId: String?,
    private val courseId: String,
    private val repo: YogaRepository
): ViewModel() {

    var teacherName by mutableStateOf(TextFieldValue())
        private set
    private val teacherNameFlow = MutableStateFlow("")

    var teacherList by mutableStateOf<List<String>>(emptyList())
        private set

    var date by mutableStateOf(TextFieldValue())
    var comment by mutableStateOf(TextFieldValue())

    var dayOfWeek by mutableStateOf(DayOfWeek.MONDAY)
        private set

    var navigateBack by mutableStateOf(false)
    var inputError by mutableStateOf<ClassInputError?>(null)

    private val _suggestions = teacherNameFlow
        .debounce(DEBOUNCE_MILLIS)
        .distinctUntilChanged()
        .flatMapLatest { if (it.isBlank()) flowOf(emptyList()) else repo.getTeacherNameSuggestions(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val suggestions = _suggestions
        .map { it.map { (_, name) -> name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        viewModelScope.launch {
            repo.getCourse(courseId)?.dayOfWeek
                ?.also { dayOfWeek = it }

            classId
                ?.let { repo.getYogaClass(it) }
                ?.also {
                    teacherList = it.teachers
                    date = TextFieldValue(it.date, TextRange(it.date.length))
                    comment = TextFieldValue(it.comment, TextRange(it.comment.length))
                }
        }
    }

    fun updateTeacherName(query: TextFieldValue) {
        teacherName = query
        teacherNameFlow.value = query.text
        resetInputError()
    }

    fun addTeacher() {
        teacherList = teacherList + teacherName.text.trim()
        updateTeacherName(TextFieldValue())
    }

    fun removeTeacher(name: String) {
        teacherList = teacherList.filter { it != name }
    }

    fun resetInputError() {
        inputError = null
    }

    fun createClass() {
        val error = validateInputs()
        if (error != null) {
            inputError = error
            return
        }

        viewModelScope.launch {
            repo.createYogaClass(
                YogaClass(
                    id = classId ?: UUID.randomUUID().toString(),
                    courseId = courseId,
                    teachers = teacherList,
                    date = date.text,
                    comment = comment.text.trim(),
                )
            ).onSuccess {
                navigateBack = true
            }
        }
    }

    private fun validateInputs(): ClassInputError? {
        if (teacherList.isEmpty()) {
            return ClassInputError.TeacherName
        }
        if (date.text.trim().isBlank()) {
            return ClassInputError.Date
        }
        return null
    }

    class Factory(
        private val classId: String?,
        private val courseId: String,
        private val repo: YogaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return CreateYogaClassViewModel(
                classId = classId,
                courseId = courseId,
                repo = repo
            ) as T
        }
    }
}