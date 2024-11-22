@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.aungpyaephyo.yoga.admin.features.search



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.aungpyaephyo.yoga.admin.data.model.YogaClass
import com.aungpyaephyo.yoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

private const val SEARCH_DEBOUNCE_MILLIS = 300L

class SearchViewModel(
    private val repo: YogaRepository
) : ViewModel() {

    var searchQuery = mutableStateOf(TextFieldValue(""))
        private set

    private val allYogaClasses = repo.getAllYogaClasses()
    private val searchQueryFlow = MutableStateFlow("")

    val suggestions = searchQueryFlow
        .debounce(SEARCH_DEBOUNCE_MILLIS)
        .distinctUntilChanged()
        .flatMapLatest { if (it.isBlank()) flowOf(emptyList()) else repo.getTeacherNameSuggestions(it) }
        .map { it.map { (_, name) -> name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    var dateFilter by mutableStateOf<String?>(null)
        private set
    private val dateFilterFlow = MutableStateFlow<String?>(null)

    var dayFilter by mutableStateOf<DayOfWeek?>(null)
        private set
    private val dayFilterFlow = MutableStateFlow<DayOfWeek?>(null)

    val searchResult: StateFlow<List<YogaClass>> = combine(
        searchQueryFlow
            .debounce(SEARCH_DEBOUNCE_MILLIS)
            .distinctUntilChanged(),
        allYogaClasses,
        dateFilterFlow,
        dayFilterFlow,
    ) { query, yogaClasses, dateFilter, dayFiler ->
        if (query.isBlank()) {
            emptyList()
        } else {
            yogaClasses.filter {
                val nameContains = it.teachers.any { teacherName ->
                    teacherName.contains(query, true)
                }
                val dateMatches = dateFilter == null || it.date == dateFilter
                val dayMatches = dayFiler == null || it.dayOfWeek == dayFiler
                nameContains && dateMatches && dayMatches
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    var confirmDeleteId = mutableStateOf<String?>(null)
        private set

    fun showConfirmDelete(id: String) {
        confirmDeleteId.value = id
    }

    fun hideConfirmDelete() {
        confirmDeleteId.value = null
    }

    fun updateQuery(query: TextFieldValue) {
        searchQuery.value = query
        searchQueryFlow.value = query.text
    }

    fun updateDateFilter(date: String?) {
        dateFilter = date
        dateFilterFlow.value = date
    }

    fun updateDayFilter(day: DayOfWeek?) {
        dayFilter = day
        dayFilterFlow.value = day
    }

    fun deleteYogaClass(classId: String) {
        viewModelScope.launch {
            repo.deleteClass(classId)
        }
    }

    class Factory(
        private val repo: YogaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return SearchViewModel(
                repo = repo
            ) as T
        }
    }
}

