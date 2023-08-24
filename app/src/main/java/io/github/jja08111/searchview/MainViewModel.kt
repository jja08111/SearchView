package io.github.jja08111.searchview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _recentQueriesState = MutableStateFlow(
        listOf(
            "Smith",
            "Wilson",
            "Michael",
            "Sophia",
            "John"
        )
    )
    val recentQueriesState: StateFlow<List<String>> = _recentQueriesState.asStateFlow()

    fun updateQuery(query: String) {
        _recentQueriesState.update { recentQueries ->
            if (recentQueries.contains(query)) {
                listOf(query) + recentQueries.filterNot { it == query }
            } else {
                listOf(query) + recentQueries
            }
        }
    }
}