package com.granah.simpletodoapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.granah.simpletodoapp.databinding.FragmentTodoTasksBinding;
import com.granah.simpletodoapp.R
import com.granah.simpletodoapp.utils.SwipeToDelete
import com.granah.simpletodoapp.adapters.TaskAdapter
import com.granah.simpletodoapp.adapters.TaskListener
import com.granah.simpletodoapp.adapters.TaskOnDelete
import com.granah.simpletodoapp.database.TaskDatabase
import com.granah.simpletodoapp.viewmodels.TodoTaskViewModel
import com.granah.simpletodoapp.viewmodels.TodoTasksViewModelFactory



class TodoTasksFragment : Fragment() {

    lateinit var  viewModel:TodoTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    val binding = DataBindingUtil.inflate<FragmentTodoTasksBinding>(inflater,R.layout.fragment_todo_tasks,container,false)

        setHasOptionsMenu(true)

        /*
            Bindings
         */
        val dataSource = TaskDatabase.getInstance(requireNotNull(this.activity).application).taskDatabaseDao
        val viewModelFactory = TodoTasksViewModelFactory(dataSource,requireNotNull(this.activity).application)
         viewModel = ViewModelProvider(this,viewModelFactory).get(TodoTaskViewModel::class.java)

        /*
              Set up viewmodel and adapters
        */
        val adapter = TaskAdapter(taskListener(), taskListenerDelete())

        binding.todoTaskViewModel = viewModel
        binding.recyclerView.adapter = adapter
        binding.lifecycleOwner = this

        val itemTouchDelete = ItemTouchHelper(SwipeToDelete(adapter))

        itemTouchDelete.attachToRecyclerView(binding.recyclerView)



        // Listeners

        binding.floatingActionButton.setOnClickListener {
             MaterialDialog(requireContext()).show {
                input(maxLength = 25,hint = getString(R.string.input_task_title)){
                        dialog, text ->  viewModel.onInsert(text.toString()) }
                 title(R.string.title_dialog)
                 positiveButton(R.string.done)
             }
        }

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


    private fun taskListener():TaskListener{
        return TaskListener{ id: Long, pos: Int -> viewModel.onCompleted(id,pos)}
    }

    private fun taskListenerDelete():TaskOnDelete{
        return TaskOnDelete{ viewModel.onDelete(it)}
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.deleteAllTasks -> viewModel.onDeleteAll()
        }

        return NavigationUI.onNavDestinationSelected(item,requireView().findNavController())
                ||super.onOptionsItemSelected(item)

    }


}