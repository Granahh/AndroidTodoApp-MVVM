package com.granah.simpletodoapp.viewmodels

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.granah.simpletodoapp.database.TaskDao
import com.granah.simpletodoapp.models.Task
import kotlinx.coroutines.*
import java.lang.Exception

class TodoTaskViewModel(private val dataSource: TaskDao, application: Application) : AndroidViewModel(application){

    private val database = dataSource

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

     val tasks: LiveData<List<Task>> = database.getTasks(false)


    fun onInsert(name:String){
        uiScope.launch {
            withContext(Dispatchers.IO){

                database.insert(Task(0,name,System.currentTimeMillis(),false))

            }
        }
    }

    fun onDeleteAll(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.deleteAll()
            }
        }
    }


    fun onDelete(taskPosition: Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val task = tasks.value?.get(taskPosition)

                if (task != null ) {
                    database.delete(task.taskId)
                }

            }
        }
    }




    fun onCompleted(taskId: Long,taskPosition: Int){

            uiScope.launch {
                withContext(Dispatchers.IO){

                    try {
                        val task = tasks.value?.get(taskPosition)
                        if(task!=null){
                            task.isDone = true
                            database.update(task)
                        } else {
                            Log.e(TAG, "onCompleted: Error Actualización")
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "onCompleted: ", e)
                    }


                }

            }
        }


    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }

    }




