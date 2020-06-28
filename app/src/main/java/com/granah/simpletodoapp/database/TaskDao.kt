package com.granah.simpletodoapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.granah.simpletodoapp.models.Task
import java.time.LocalDate

@Dao
interface TaskDao {

    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Query("SELECT * FROM task_table where is_done = :isDone ORDER BY created_at_task DESC")
    fun getTasks(isDone: Boolean): LiveData<List<Task>>

    @Query("DELETE FROM task_table")
    fun deleteAll()

    @Query("DELETE FROM task_table where taskId =:taskId")
    fun delete(taskId: Long)



}