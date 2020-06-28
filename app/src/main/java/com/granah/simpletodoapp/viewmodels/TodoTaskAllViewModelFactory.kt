package com.granah.simpletodoapp.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.granah.simpletodoapp.database.TaskDao

class TodoTaskAllViewModelFactory(private val dataSource: TaskDao, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoTaskAllViewModel::class.java)){
            return TodoTaskAllViewModel(dataSource,application) as T
        }

        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}