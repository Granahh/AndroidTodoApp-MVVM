package com.granah.simpletodoapp.viewmodels

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.granah.simpletodoapp.database.TaskDao
import kotlinx.coroutines.*
import java.lang.Exception

class TodoTaskAllViewModel(dataSource: TaskDao, application: Application) : AndroidViewModel(application) {
    private val database = dataSource

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var tasks = database.getTasks(true)


    fun onDelete(position: Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val task =tasks.value?.get(position)
                if (task != null) {
                    database.delete(task.taskId)
                }

            }
        }
    }

    fun onUnChecked(taskId: Long,taskPosition: Int){

        uiScope.launch {
            withContext(Dispatchers.IO){

                try {
                    val task = tasks.value?.get(taskPosition)
                    if(task!=null){
                        task.isDone = false
                        database.update(task)
                    } else {
                        Log.e(ContentValues.TAG, "onCompleted: Error Actualización")
                    }

                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "onCompleted: ", e)
                }


            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}