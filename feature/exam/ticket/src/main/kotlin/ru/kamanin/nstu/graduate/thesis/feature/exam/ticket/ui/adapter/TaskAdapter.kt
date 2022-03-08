package ru.kamanin.nstu.graduate.thesis.feature.exam.ticket.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kamanin.nstu.graduate.thesis.component.core.recyclerview.DiffUtilCallback
import ru.kamanin.nstu.graduate.thesis.feature.exam.ticket.ui.model.TaskItem

class TaskAdapter(private val taskClicked: (TaskItem) -> Unit) : RecyclerView.Adapter<TaskViewHolder>() {

	var items: List<TaskItem> = emptyList()
		set(value) {
			field = value
			diff.getDiffResult(value, detectMoves = false).dispatchUpdatesTo(this)
		}

	private val diff = DiffUtilCallback<TaskItem> { old, new -> old.id == new.id }

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
		TaskViewHolder(parent)

	override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
		holder.bind(items[position], taskClicked)
	}

	override fun getItemCount(): Int = items.size
}