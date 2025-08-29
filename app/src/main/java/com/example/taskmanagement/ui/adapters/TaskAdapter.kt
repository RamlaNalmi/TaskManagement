package com.example.taskmanagement.ui.adapters



// TaskAdapter.kt
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.R
import com.example.taskmanagement.ui.viewmodels.TaskViewModel
import com.example.taskmanagement.ui.activities.UpdateTaskActivity
import com.example.taskmanagement.database.entities.Task

class TaskAdapter(private val taskViewModel: TaskViewModel, private val name: String) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(
    TaskDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.bind(currentTask)
    }

    private val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val taskViewHolder = viewHolder as TaskViewHolder
            taskViewHolder.showDeleteButton() // Show delete button
            notifyItemChanged(position) // Notify adapter to redraw the item
        }
    }


    private val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        itemTouchHelper.attachToRecyclerView(recyclerView) // Attach swipe-to-reveal functionality to RecyclerView
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskNameTextView: TextView = itemView.findViewById(R.id.textTaskName)
        private val dateTextView: TextView = itemView.findViewById(R.id.textDate)
        private val timeTextView: TextView = itemView.findViewById(R.id.textTime)
        private val completeCheckBox: CheckBox = itemView.findViewById(R.id.checkboxComplete)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun showDeleteButton() {
            deleteButton.visibility = View.VISIBLE
        }
        init {

            completeCheckBox.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = getItem(position)
                    taskViewModel.updateTaskCompletionAndDate(task.id,task.userId, isChecked)
                }
            }

            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = getItem(position)
                    deleteTask(task)
                }
            }

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = getItem(position)
                    openUpdateTaskActivity(task.id, task.userId, name) // Pass the taskId
                }
            }
        }



        private fun deleteTask(task: Task) {
            taskViewModel.deleteTask(task)
        }

        private fun openUpdateTaskActivity(taskId: Int, userId: Int, name:String) {
            val context = itemView.context
            val intent = Intent(context, UpdateTaskActivity::class.java)
            intent.putExtra("taskId", taskId)
            intent.putExtra("userId", userId)
            intent.putExtra("name", name)
            context.startActivity(intent)
        }

        fun bind(task: Task) {
            taskNameTextView.text = task.name
            dateTextView.text = task.date
            timeTextView.text = task.time
            completeCheckBox.isChecked = task.completed
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }


}