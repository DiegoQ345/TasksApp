package com.quispe.tasksapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quispe.tasksapp.data.Task
import com.quispe.tasksapp.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _filter = MutableStateFlow("")
    private val _orderBy = MutableStateFlow("date")

    val tasks = combine(_filter, _orderBy) { filter, orderBy ->
        repository.getTasks(filter, orderBy)
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setFilter(value: String) {
        _filter.value = value
    }

    fun setOrderBy(value: String) {
        _orderBy.value = value
    }

    fun addTask(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }
}