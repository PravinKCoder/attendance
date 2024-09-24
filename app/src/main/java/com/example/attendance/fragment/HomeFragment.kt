package com.example.attendance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.attendance.R
import com.example.attendance.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Attendance"
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.cvAdmission.setOnClickListener {
            val bundle = Bundle().apply {
                //putParcelable("student_key",student)
                putBoolean("student_key", false)
            }
            findNavController().navigate(R.id.action_homeFragment_to_admissionFragment)
        }

        binding.cvStudentsRecords.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_studentRecordsFragment)
        }

        binding.cvAttendance.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_attendanceFragment)
        }

        binding.cvAttendanceRecords.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_attendanceRecordsFragment)
        }
    }

}