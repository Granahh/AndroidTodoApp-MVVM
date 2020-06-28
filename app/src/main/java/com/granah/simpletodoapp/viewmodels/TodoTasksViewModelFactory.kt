package com.granah.simpletodoapp.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.granah.simpletodoapp.database.TaskDao


class TodoTasksViewModelFactory(private val dataSource: TaskDao, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoTaskViewModel::class.java)){
            return TodoTaskViewModel(dataSource,application) as T
        }

        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}