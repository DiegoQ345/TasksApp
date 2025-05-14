package com.quispe.tasksapp.repository

import com.quispe.tasksapp.data.Task
import com.quispe.tasksapp.data.TaskDao
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    fun getTasks(filter: String, orderBy: String): Flow<List<Task>> =
        dao.getFilteredAndOrderedTasks(filter, orderBy)

    suspend fun insert(task: Task) = dao.insert(task)
    suspend fun delete(task: Task) = dao.delete(task)
}