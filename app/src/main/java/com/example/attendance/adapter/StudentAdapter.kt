package com.example.attendance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance.R
import com.example.attendance.databinding.ItemStudentBinding
import com.example.attendance.model.Student

class StudentAdapter(
    private var students: List<Student>,
    var onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemStudentBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_student, parent, false)
        return ViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size

    class ViewHolder(
        private val binding: ItemStudentBinding,
        val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.student = student
            binding.executePendingBindings()
            binding.tvView.setOnClickListener {
                onItemClickListener.onClickBottomView(student)
            }
            binding.ivEdit.setOnClickListener {
                onItemClickListener.onEditClick(student)
            }
        }
    }

    fun setFilterList(filterList: ArrayList<Student>) {
        this.students = filterList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClickBottomView(student: Student)
        fun onEditClick(student: Student)
    }

}