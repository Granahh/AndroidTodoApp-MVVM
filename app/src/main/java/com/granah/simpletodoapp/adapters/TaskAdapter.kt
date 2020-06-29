package com.granah.simpletodoapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.granah.simpletodoapp.databinding.ListItemTaskBinding
import com.granah.simpletodoapp.models.Task
import java.text.DateFormat

class TaskAdapter(private val onDeleteListener:TaskAdapterSwipeListener, private val clickListener: TaskAdapterListeners) : ListAdapter<Task, TaskAdapter.ViewHolder>(TaskDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }

    fun onDelete(position: Int) = onDeleteListener.onSwipeDelete(position)

    class ViewHolder private constructor(private val binding: ListItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item:Task,clickListener: TaskAdapterListeners){

            binding.task = item
            binding.clicklistener = clickListener
            binding.position = this
            binding.dateTime = DateFormat.getInstance().format(item.createdAt).toString()

            binding.checkbox.isChecked = item.isDone
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }
}



class TaskDiffCallback : DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}


interface TaskAdapterListeners{
    fun onClickCheckBox(task: Task, position: Int)

}

interface TaskAdapterSwipeListener{
    fun onSwipeDelete(position: Int)
}

