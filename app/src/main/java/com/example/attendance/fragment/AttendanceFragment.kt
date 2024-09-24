package com.example.attendance.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.attendance.MainApplication
import com.example.attendance.adapter.DailyAttendanceAdapter
import com.example.attendance.databinding.FragmentAttendanceBinding
import com.example.attendance.model.Attendance
import com.example.attendance.model.Student
import com.example.attendance.viewmodel.MainViewModel
import com.example.attendance.viewmodel.MainViewModelFactory
import kotlinx.coroutines.Deferred
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AttendanceFragment : Fragment(), DailyAttendanceAdapter.OnClickAttendanceListener {
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var viewModel: MainViewModel
    private var attendance = ArrayList<Attendance>()

    // var attendance = listOf(Attendance())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialize() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Daily Attendance"
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = (requireActivity().application as MainApplication).classRepository
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]
        //  binding.recyclerViewAttendance.layoutManager = LinearLayoutManager(requireContext())

        val student = ArrayList<Student>()
        //val adapter = DailyAttendanceAdapter(student)

        // var data = viewModel.getAttendanceRecord()

        /*viewModel.getAttendanceRecord().observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                attendance = it as ArrayList<Attendance>
        }*/
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(formatter)
        var result: Deferred<Unit>
       /* lifecycleScope.launch {
            result = async {
                viewModel.getAttendanceRecord(formattedDate).observe(viewLifecycleOwner) {
                    if (it.isNotEmpty())
                        attendance = it as ArrayList<Attendance>
                }
            }
            result.await()
        }*/
        viewModel.studentsRecord.observe(viewLifecycleOwner) { student ->
   /*         if (attendance.isNotEmpty()) {
                *//*  for (stud in student) {
                 // var name = stud.firstName + stud.middleName + stud.lastName
                 var name = stud.toString()
                 attendance = arrayListOf(Attendance(stud.id.toString(), name))
             }*//*
                binding.recyclerViewAttendance.adapter = DailyAttendanceAdapter(attendance, this)
            } else {*/
                for ((index, stud) in student.withIndex()) {
                    // var name = stud.firstName + stud.middleName + stud.lastName
                    var name = stud.toString()
                    //attendance = arrayListOf(Attendance(stud.id.toString(), name))
                    //attendance = arrayListOf(Attendance(stud.id.toString(), name))
                    attendance.add(index, Attendance(stud.id.toString(), name))
                }
                binding.recyclerViewAttendance.adapter = DailyAttendanceAdapter(attendance, this)
           // }
        }

        /*   adapter.setOnClickListener(object : DailyAttendanceAdapter.OnClickAttendanceListener {
               override fun onClick(student: Student) {
                   Toast.makeText(requireContext(), student.firstName, Toast.LENGTH_SHORT).show()
               }
           })*/
        // TODO: use for loop to insert list attendance use progress bar to show submit data
        /* binding.btnSubmitAttedance.setOnClickListener {
             viewModel.insertDailyAttendance(attendance)
         }*/
        binding.btnSubmitAttedance.setOnClickListener {
            var isSubmitAttendance = true
            for (attendanceList in attendance) {
                if (attendanceList.attendance == "") {
                    val name = attendanceList.studentName
                    Toast.makeText(
                        requireContext(),
                        "You have not Mark attendance $name",
                        Toast.LENGTH_SHORT
                    ).show()
                    isSubmitAttendance = false
                    break
                } else {
                    isSubmitAttendance = true
                }
            }
            if (isSubmitAttendance) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(student: Attendance, attendanceMark: String) {
        val currentDate = LocalDate.now()
        val currentDay = currentDate.dayOfMonth
        val currentMonth = currentDate.month.name.toLowerCase().capitalize() // e.g., "August"
        val currentYear = currentDate.year
        // Format the current date as a string
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(formatter)
        //student.isAttendance = attendanceMark
        //viewModel.update(student)
        val attendance = Attendance(
            studentId = student.studentId,
            studentName = student.studentName,
            month = currentMonth,
            year = currentYear.toString(),
            date = formattedDate,
            attendance = attendanceMark
        )
        viewModel.insertDailyAttendance(attendance)
        //Toast.makeText(requireContext(), student.firstName, Toast.LENGTH_SHORT).show()
    }
}