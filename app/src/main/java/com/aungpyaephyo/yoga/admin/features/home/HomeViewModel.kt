package com.aungpyaephyo.yoga.admin.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.aungpyaephyo.yoga.admin.data.model.YogaCourse
import com.aungpyaephyo.yoga.admin.data.repo.YogaRepository
import com.aungpyaephyo.yoga.admin.SyncDataUseCase

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: YogaRepository,
    private val syncDataUseCase: SyncDataUseCase,
) : ViewModel() {

    val courses: StateFlow<List<YogaCourse>> = repo.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    var confirmDeleteId = mutableStateOf<String?>(null)
        private set

    var confirmUpload = mutableStateOf(false)
        private set

    var uploadError by mutableStateOf<Throwable?>(null)
    var uploadSuccess by mutableStateOf(false)

    fun showConfirmDelete(id: String) {
        confirmDeleteId.value = id
    }

    fun hideConfirmDelete() {
        confirmDeleteId.value = null
    }

    fun showConfirmUpload() {
        confirmUpload.value = true
    }

    fun hideConfirmUpload() {
        confirmUpload.value = false
    }

    fun deleteCourse(courseId: String) {
        viewModelScope.launch {
            repo.deleteCourse(courseId)
        }
    }

    fun uploadDataToServer(skipEvents: Boolean = false) {
        viewModelScope.launch {
            syncDataUseCase.uploadToFireStore(courses.value, true)
                .onSuccess {
                    if (!skipEvents) {
                        uploadSuccess = true
                    }
                }
                .onFailure { e ->
                    if (!skipEvents) {
                        uploadError = e
                    }
                }
        }
    }

    class Factory(
        private val repo: YogaRepository,
        private val syncDataUseCase: SyncDataUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return HomeViewModel(
                repo = repo,
                syncDataUseCase = syncDataUseCase,
            ) as T
        }
    }
}

