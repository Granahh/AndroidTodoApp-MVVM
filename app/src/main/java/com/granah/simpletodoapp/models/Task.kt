package com.granah.simpletodoapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L ,
    @ColumnInfo(name="name_task")
    var name:String = "",
    @ColumnInfo(name="created_at_task")
    var createdAt:Long,
    @ColumnInfo(name="is_done")
    var isDone : Boolean
    )
