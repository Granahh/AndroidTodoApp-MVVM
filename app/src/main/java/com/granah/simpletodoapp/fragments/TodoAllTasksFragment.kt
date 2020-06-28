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
import com.granah.simpletodoapp.utils.SwipeToDelete
import com.granah.simpletodoapp.adapters.TaskAdapter
import com.granah.simpletodoapp.adapters.TaskListener
import com.granah.simpletodoapp.adapters.TaskOnDelete
import com.granah.simpletodoapp.database.TaskDatabase
import com.granah.simpletodoapp.databinding.FragmentTodoAllTasksBinding
import com.granah.simpletodoapp.viewmodels.TodoTaskAllViewModel
import com.granah.simpletodoapp.viewmodels.TodoTaskAllViewModelFactory


class TodoAllTasksFragment : Fragment() {


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
        val viewModel = ViewModelProvider(this,viewModelFactory).get(TodoTaskAllViewModel::class.java)

        val adapter = TaskAdapter(TaskListener{ l: Long, i: Int ->
           viewModel.onUnChecked(l,i)

        }, TaskOnDelete {
            viewModel.onDelete(it)
        })

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




}