package ru.kamanin.nstu.graduate.thesis.feature.exam.list.ui.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kamanin.nstu.graduate.thesis.component.ui.core.colors.colorFromAttr
import ru.kamanin.nstu.graduate.thesis.component.ui.core.utils.inflate
import ru.kamanin.nstu.graduate.thesis.feature.exam.list.R
import ru.kamanin.nstu.graduate.thesis.feature.exam.list.databinding.ItemExamBinding
import ru.kamanin.nstu.graduate.thesis.shared.exam.domain.entity.Exam
import ru.kamanin.nstu.graduate.thesis.shared.exam.domain.entity.Exam.*
import ru.kamanin.nstu.graduate.thesis.shared.exam.domain.entity.ExamState
import ru.kamanin.nstu.graduate.thesis.utils.time.dateStringFormat

class ExamViewHolder(private val parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_exam)) {

	private companion object {

		const val TIME_NOT_SET = 0L
	}

	private val viewBinding by viewBinding(ItemExamBinding::bind)

	fun bind(exam: Exam, onExamClick: (Exam) -> Unit) {
		with(viewBinding) {
			examName.text = exam.name
			examTeacher.text = exam.teacher.toSingleLine()
			examMark.text = parent.context.getString(R.string.hint_exam_mark, exam.mark)
			examDateTime.text = if (exam.period.start != TIME_NOT_SET) {
				parent.context.getString(R.string.hint_exam_date_time, exam.period.start.dateStringFormat)
			} else {
				parent.context.getString(R.string.hint_exam_date_time_prepare)
			}
			root.setOnClickListener { onExamClick(exam) }

			if (exam.examState == ExamState.PROGRESS) {
				root.setBackgroundColor(parent.context.colorFromAttr(R.attr.colorBackgroundStateTint))
			} else {
				root.setBackgroundColor(ContextCompat.getColor(parent.context, android.R.color.transparent))
			}
		}
	}

	private fun Teacher.toSingleLine() =
		parent.context.getString(R.string.hint_exam_teacher, account.surname, account.name.first())
}