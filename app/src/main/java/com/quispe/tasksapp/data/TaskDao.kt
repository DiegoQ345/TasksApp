package com.quispe.tasksapp.data


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :filter || '%' ORDER BY " +
            "CASE WHEN :orderBy = 'date' THEN timestamp END ASC, " +
            "CASE WHEN :orderBy = 'priority' THEN priority END ASC")
    fun getFilteredAndOrderedTasks(filter: String, orderBy: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)
}
