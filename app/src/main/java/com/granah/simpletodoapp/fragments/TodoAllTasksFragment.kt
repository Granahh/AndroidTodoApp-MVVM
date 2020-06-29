package com.granah.simpletodoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.granah.simpletodoapp.R
import com.granah.simpletodoapp.adapters.*
import com.granah.simpletodoapp.utils.SwipeToDelete
import com.granah.simpletodoapp.database.TaskDatabase
import com.granah.simpletodoapp.databinding.FragmentTodoAllTasksBinding
import com.granah.simpletodoapp.models.Task
import com.granah.simpletodoapp.viewmodels.TodoTaskAllViewModel
import com.granah.simpletodoapp.viewmodels.TodoTaskAllViewModelFactory


class TodoAllTasksFragment : Fragment(), TaskAdapterListeners,TaskAdapterSwipeListener {
    private lateinit var viewModel:TodoTaskAllViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTodoAllTasksBinding>(inflater,R.layout.fragment_todo_all_tasks,container,false)

        /*
          Bindings
       */
        val dataSource = TaskDatabase.getInstance(requireNotNull(this.activity).application).taskDatabaseDao
        val viewModelFactory = TodoTaskAllViewModelFactory(dataSource,requireNotNull(this.activity).application)

         viewModel = ViewModelProvider(this,viewModelFactory).get(TodoTaskAllViewModel::class.java)

        val adapter = TaskAdapter(this,this)

        binding.todoTaskAllViewModel = viewModel
        binding.recyclerView.adapter = adapter

        binding.lifecycleOwner = this

        val itemTouchDelete = ItemTouchHelper(
            SwipeToDelete(
                adapter
            )
        )

        itemTouchDelete.attachToRecyclerView(binding.recyclerView)

        /*
           Observers
        */

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)


                if(it.isNotEmpty())
                    binding.tvNullTasks.visibility = View.INVISIBLE
                else
                    binding.tvNullTasks.visibility = View.VISIBLE
            }


        })

        return binding.root
    }

    override fun onClickCheckBox(task: Task, position: Int) {
        viewModel.onUnChecked(task.taskId,position)
    }

    override fun onSwipeDelete(position: Int) {
        viewModel.onDelete(position)
    }


}