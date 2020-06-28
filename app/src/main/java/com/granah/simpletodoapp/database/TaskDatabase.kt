package com.granah.simpletodoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.granah.simpletodoapp.models.Task

@Database(entities = [Task::class],version = 1)
abstract class TaskDatabase : RoomDatabase(){
    abstract val taskDatabaseDao : TaskDao

    companion object{
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context) : TaskDatabase {
            synchronized(this){


                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,TaskDatabase::class.java,"todo_task_database"
                    ).build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}