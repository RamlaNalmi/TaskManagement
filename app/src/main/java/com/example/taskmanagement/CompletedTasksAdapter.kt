package com.example.taskmanagement



import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.R
import com.example.taskmanagement.database.entities.Task

class CompletedTasksAdapter(private val taskViewModel: TaskViewModel)  : ListAdapter<Task, CompletedTasksAdapter.CompletedTaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.completed_task_item, parent, false)
        return CompletedTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompletedTaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class CompletedTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskNameTextView: TextView = itemView.findViewById(R.id.textTaskName)
        private val taskDateTextView: TextView = itemView.findViewById(R.id.textDate)
        private val taskTimeTextView: TextView = itemView.findViewById(R.id.textTime)
        private val checkBoxComplete: CheckBox = itemView.findViewById(R.id.checkboxComplete)



        init {
            checkBoxComplete.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = getItem(position)
                    taskViewModel.updateTaskCompletionAndDate(task.id,task.userId, isChecked)
                }
            }
        }


        fun bind(task: Task) {
            taskNameTextView.text = task.name
            taskDateTextView.text = task.date
            taskTimeTextView.text = task.time
            checkBoxComplete.isChecked = task.completed


            if (task.completed) {
                taskNameTextView.paintFlags = taskNameTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                // Remove strike-through style if task is not completed
                taskNameTextView.paintFlags = taskNameTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
