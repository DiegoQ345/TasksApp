package com.quispe.tasksapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.quispe.tasksapp.data.AppDatabase
import com.quispe.tasksapp.repository.TaskRepository
import com.quispe.tasksapp.screen.TaskScreen
import com.quispe.tasksapp.viewmodel.TaskViewModel
import com.quispe.tasksapp.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "task_db"
        ).build()

        val repository = TaskRepository(database.taskDao())
        val factory = TaskViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        setContent {
            MaterialTheme {
                TaskScreen(viewModel)
            }
        }
    }
}