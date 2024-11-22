package com.aungpyaephyo.yoga.admin.features.course.detail
import com.aungpyaephyo.yoga.admin.data.repo.YogaRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch

class CourseDetailViewModel(
    private val courseId: String,
    private val repo: YogaRepository
) : ViewModel() {

    val course = repo.getCourseDetails(courseId)

    var confirmDeleteId = mutableStateOf<String?>(null)
        private set

    fun showConfirmDelete(id: String) {
        confirmDeleteId.value = id
    }

    fun hideConfirmDelete() {
        confirmDeleteId.value = null
    }

    fun deleteClass(classId: String) {
        viewModelScope.launch {
            repo.deleteClass(classId)
        }
    }

    class Factory(
        private val courseId: String,
        private val repo: YogaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return CourseDetailViewModel(
                courseId = courseId,
                repo = repo
            ) as T
        }
    }
}