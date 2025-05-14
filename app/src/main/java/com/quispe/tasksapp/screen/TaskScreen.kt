package com.quispe.tasksapp.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quispe.tasksapp.data.Task
import com.quispe.tasksapp.viewmodel.TaskViewModel

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var filter by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TextField(
                value = filter,
                onValueChange = {
                    filter = it
                    viewModel.setFilter(it)
                },
                label = { Text("Buscar tarea...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.setOrderBy("date") }) {
                    Text("Ordenar por Fecha")
                }
                Button(onClick = { viewModel.setOrderBy("priority") }) {
                    Text("Ordenar por Prioridad")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(tasks) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(task.title, style = MaterialTheme.typography.titleMedium)
                            task.description?.let { Text(it) }
                            Text("Prioridad: ${task.priority}")
                        }
                    }
                }
            }
        }

        // Mostrar diálogo de nueva tarea
        if (showDialog) {
            NewTaskDialog(
                onDismiss = { showDialog = false },
                onAdd = { title, description, priority ->
                    viewModel.addTask(Task(title = title, description = description, priority = priority))
                    showDialog = false
                }
            )
        }
    }
}



@Composable
fun NewTaskDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String?, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(2) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title, description.takeIf { it.isNotBlank() }, priority)
                    }
                }
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Nueva tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") }
                )
                Spacer(Modifier.height(8.dp))
                Text("Prioridad:")
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf(1 to "Alta", 2 to "Media", 3 to "Baja").forEach { (value, label) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = priority == value,
                                onClick = { priority = value }
                            )
                            Text(label)
                        }
                    }
                }
            }
        }
    )
}
