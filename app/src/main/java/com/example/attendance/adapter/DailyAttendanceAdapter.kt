package com.example.attendance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance.R
import com.example.attendance.databinding.ListDailyAttendanceBinding
import com.example.attendance.model.Attendance

class DailyAttendanceAdapter(
    private var students: List<Attendance>,
    private val onClickAttendanceListener: OnClickAttendanceListener?
) :
    RecyclerView.Adapter<DailyAttendanceAdapter.ViewHolder>() {
    //private var onClickAttendanceListener: OnClickAttendanceListener?=null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListDailyAttendanceBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_daily_attendance, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(students[position], onClickAttendanceListener)
    }

    override fun getItemCount(): Int = students.size
    fun setFilterList(filterList: ArrayList<Attendance>) {
        this.students = filterList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListDailyAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(attendance: Attendance, onClickAttendanceListener: OnClickAttendanceListener?) {
            binding.student = attendance
            if (attendance.attendance.isNotEmpty())
                setViews(attendance, attendance.attendance)

            binding.ivAttendancePresent.setOnClickListener {
                setViews(attendance, "P")
                onClickAttendanceListener?.onClick(attendance, "P")
                /*  binding.ivAttendancePresent.setBackgroundResource(R.color.background1)
                  binding.ivAttendanceAbsent.setBackgroundResource(R.color.text_red)
                  //binding.cvView.setBackgroundResource(R.drawable.border1)
                  binding.cvView.setBackgroundResource(R.color.cv_green)*/
            }
            binding.ivAttendanceAbsent.setOnClickListener {
                setViews(attendance, "A")
                onClickAttendanceListener?.onClick(attendance, "A")
                /*   binding.ivAttendanceAbsent.setBackgroundResource(R.color.background2)
                   binding.ivAttendancePresent.setBackgroundResource(R.color.text_green)
                   //binding.cvView.setBackgroundResource(R.drawable.border2)
                   binding.cvView.setBackgroundResource(R.color.cv_red)*/
            }
        }

        private fun setViews(attendance: Attendance, attendanceM: String) {
            if (attendanceM == "P") {
                binding.ivAttendancePresent.setBackgroundResource(R.color.background1)
                binding.ivAttendanceAbsent.setBackgroundResource(R.color.text_red)
                //binding.cvView.setBackgroundResource(R.drawable.border1)
                binding.cvView.setBackgroundResource(R.color.cv_green)
            } else {
                binding.ivAttendanceAbsent.setBackgroundResource(R.color.background2)
                binding.ivAttendancePresent.setBackgroundResource(R.color.text_green)
                //binding.cvView.setBackgroundResource(R.drawable.border2)
                binding.cvView.setBackgroundResource(R.color.cv_red)
            }

        }
    }


    interface OnClickAttendanceListener {
        fun onClick(student: Attendance, attendanceMark: String)
    }

    /*  fun setOnClickListener(listener: OnClickAttendanceListener) {
          this.onClickAttendanceListener = listener
      }*/
}