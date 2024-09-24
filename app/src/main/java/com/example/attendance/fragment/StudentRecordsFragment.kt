package com.example.attendance.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance.InfoBottomSheetDialog
import com.example.attendance.MainApplication
import com.example.attendance.R
import com.example.attendance.adapter.StudentAdapter
import com.example.attendance.databinding.FragmentStudentRecordsBinding
import com.example.attendance.model.Student
import com.example.attendance.viewmodel.MainViewModel
import com.example.attendance.viewmodel.MainViewModelFactory

class StudentRecordsFragment : Fragment(), StudentAdapter.OnItemClickListener {
    lateinit var binding: FragmentStudentRecordsBinding
    private lateinit var viewModel: MainViewModel
    private var studentList =
        ArrayList<Student>() //     private val student: ArrayList<Student> = ArrayList()
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_student_records, container, false)
        binding = FragmentStudentRecordsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initilize()
    }

    private fun initilize() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Students"
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = (requireActivity().application as MainApplication).classRepository
        viewModel =
            ViewModelProvider(
                requireActivity(),
                MainViewModelFactory(repository)
            ).get(MainViewModel::class.java)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.studentsRecord.observe(viewLifecycleOwner, Observer { students ->
            studentList = students as ArrayList<Student>
            adapter = StudentAdapter((students), this)
            binding.recyclerView.adapter = adapter
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

    }

    private fun filterList(query: String?) {
        query?.let {
            val filterList = ArrayList<Student>() // TODO: you can use filter
            for (i in studentList) {
                if (i.firstName.lowercase().contains(query))
                    filterList.add(i)
            }
            if (filterList.isEmpty()) {
                Toast.makeText(requireContext(), "No Record Found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilterList(filterList)
            }
        }
    }

    override fun onClickBottomView(student: Student) {
        val bottom = InfoBottomSheetDialog(student)
        requireActivity().supportFragmentManager.let { bottom.show(it, InfoBottomSheetDialog.TAG) }
    }

    override fun onEditClick(student: Student) {
        viewModel.setSelectedStudent(student) // this for the set data to view model and observe
        val bundle = Bundle().apply {
            putParcelable("student_key",student)
            //putBoolean("student_key", true)
        }
        findNavController().navigate(
            R.id.action_studentRecordsFragment_to_admissionFragment,
            bundle
        )
    }

    /* private var clickListener = object : StudentAdapter.OnItemClickListener{
         override fun onClickBottomView(student: Student) {
             TODO("Not yet implemented")
         }

         override fun onEditClick(student: Student) {
             TODO("Not yet implemented")
         }

     }*/
}