package com.example.attendance.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.attendance.MainApplication
import com.example.attendance.databinding.FragmentAdmissionBinding
import com.example.attendance.model.Student
import com.example.attendance.viewmodel.MainViewModel
import com.example.attendance.viewmodel.MainViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdmissionFragment : Fragment() {
    private lateinit var binding: FragmentAdmissionBinding
    private lateinit var viewModel: MainViewModel
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdmissionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Admission"
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // val repository = (requireActivity().application as MainApplication).classRepository
        //val dao = ClassDatabase.getInstance(requireContext()).getStudentDao()
        //val repository = ClassRepository(dao)
        val repository = (requireActivity().application as MainApplication).classRepository
        viewModel =
            ViewModelProvider(
                this, // if you want observe data from fragment to fragment to use //requireActivity()
                MainViewModelFactory(repository)
            ).get(MainViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        // gender set
        val list = listOf("Male", "Female")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, list)
        binding.genderDropdown.setAdapter(adapter)

        // standard
        val clasList = listOf("1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th")
        val adapterClass =
            ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, clasList)
        binding.atvClass.setAdapter(adapterClass)


        binding.dateOfBirthEditText.setOnClickListener {
            showDatePicker()
        }

        binding.submitButton.setOnClickListener {
            viewModel.registerStudent.observe(viewLifecycleOwner, Observer {
                validateStudentData(it)
            })
        }


        // this for the set data to view model and observe
        /*  viewModel.selectedItem.observe(requireActivity()) { studentUpdate ->
              viewModel.registerStudent.postValue(studentUpdate)
              val pos = adapterClass.getPosition(studentUpdate?.sClass)
              binding.atvClass.setText(studentUpdate?.sClass,false)
              binding.genderDropdown.setText(studentUpdate?.gender,false)
          }*/
        arguments?.getParcelable<Student>("student_key")?.let { studentUpdate ->
            isUpdate = true
            viewModel.registerStudent.postValue(studentUpdate)
            binding.atvClass.post {
                binding.atvClass.setText(studentUpdate.sClass, false)
                binding.atvClass.setSelection(binding.atvClass.text.length)
            }
            binding.genderDropdown.post {
                binding.genderDropdown.setText(studentUpdate.gender, false)
                binding.genderDropdown.setSelection(binding.genderDropdown.text.length)
            }
            // binding.atvClass.setText(studentUpdate.sClass, false)
            // binding.genderDropdown.setText(studentUpdate.gender, false)
        }


        /*     viewModel.isSubmit.observe(viewLifecycleOwner) { isSubmitted ->
                     Toast.makeText(requireActivity(), isSubmitted, Toast.LENGTH_SHORT).show()
             }
     */
    }

    private fun validateStudentData(student: Student) {
        //student.gender="Male"
        val message = when {
            student.gender.isEmpty() -> "Please select gender."
            student.firstName.isEmpty() -> "Please fill first name."
            student.middleName.isEmpty() -> "Please fill middle name."
            student.lastName.isEmpty() -> "Please fill last name."
            student.dob.isEmpty() -> "Please select Date Of Birth."
            student.mobileNo.isEmpty() -> "Please fill Mob No."
            student.mobileNo.length < 10 -> "Please fill valid Mob No."
            student.sClass.isEmpty() -> "Please fill Class name"
            student.school.isEmpty() -> "Please fill school name"
            student.address.isEmpty() -> "Please fill address"
            else -> if (!isUpdate) "Form Submitted Successfully" else "Form Update Successfully"
        }
        // You can display a message or take any other action based on the validation result
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        when (message) {
            "Form Submitted Successfully" -> {
                viewModel.submitData(student)
                /*    if (!isUpdate)
                        viewModel.submitData(student)
                    else
                        viewModel.update(student)
                    requireActivity().supportFragmentManager.popBackStack()*/
                requireActivity().supportFragmentManager.popBackStack()
            }

            "Form Update Successfully" -> {
                viewModel.update(student)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun onClickSubmit(submitButton: MaterialButton) {

    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date of Birth")
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val selectedDate = sdf.format(Date(selection))
            viewModel.registerStudent.value?.dob = selectedDate
            viewModel.registerStudent.postValue(viewModel.registerStudent.value)
            // binding.dateOfBirthEditText.setText(selectedDate)
        }
        datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
    }

}