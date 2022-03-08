package ru.kamanin.nstu.graduate.thesis.feature.exam.list.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kamanin.nstu.graduate.thesis.component.core.time.dateStringFormat
import ru.kamanin.nstu.graduate.thesis.component.ui.colors.colorFromAttr
import ru.kamanin.nstu.graduate.thesis.feature.exam.list.R
import ru.kamanin.nstu.graduate.thesis.feature.exam.list.databinding.ItemExamBinding
import ru.kamanin.nstu.graduate.thesis.shared.exam.domain.entity.Exam

class ExamViewHolder(private val parent: ViewGroup) : RecyclerView.ViewHolder(getView(parent)) {

	private companion object {

		fun getView(parent: ViewGroup): View =
			LayoutInflater.from(parent.context)
				.inflate(R.layout.item_exam, parent, false)
	}

	private val viewBinding by viewBinding(ItemExamBinding::bind)

	fun bind(exam: Exam, onExamClick: (Exam) -> Unit) {
		with(viewBinding) {
			examName.text = exam.name
			examTeacher.text = exam.teacher.toSingleLine()
			examMark.text = parent.context.getString(R.string.hint_exam_mark, exam.mark)
			examDateTime.text = parent.context.getString(R.string.hint_exam_date_time, exam.period.start.dateStringFormat)
			root.setOnClickListener { onExamClick(exam) }

			//TODO allowed отвечает за доступность экзамена, переписать выделение цветом на основе времени
			if (exam.allowed) {
				root.setBackgroundColor(parent.context.colorFromAttr(R.attr.colorBackgroundStateTint))
			} else {
				root.setBackgroundColor(ContextCompat.getColor(parent.context, android.R.color.transparent))
			}
		}
	}

	@SuppressLint("StringFormatInvalid")
	private fun Exam.Teacher.toSingleLine() =
		parent.context.getString(R.string.hint_exam_teacher, surname, name.first())
}