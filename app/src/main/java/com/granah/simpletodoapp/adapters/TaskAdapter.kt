package com.granah.simpletodoapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.granah.simpletodoapp.databinding.ListItemTaskBinding
import com.granah.simpletodoapp.models.Task
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskAdapter(private val clickListener: TaskListener, private val onDeleteListener:TaskOnDelete) : ListAdapter<Task, TaskAdapter.ViewHolder>(TaskDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }

    fun onDelete(position: Int) = onDeleteListener.onDelete(position)

    class ViewHolder private constructor(private val binding: ListItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item:Task,clickListener: TaskListener){

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


class TaskListener(val clickListener: (taskId:Long,position: Int) -> Unit){
    fun onClick(task:Task,position: TaskAdapter.ViewHolder) = clickListener(task.taskId,position.adapterPosition)
}

class TaskOnDelete(val onDeleteListener: (position:Int) -> Unit){
    fun onDelete(position: Int) = onDeleteListener(position)
}

