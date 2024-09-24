package com.example.attendance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendance.model.Attendance
import com.example.attendance.model.Student
import com.example.attendance.repository.ClassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val classRepository: ClassRepository) : ViewModel() {
    //private val repository: ClassRepository

    // val registerStudent = MutableLiveData<Student>()
    val registerStudent: MutableLiveData<Student> = MutableLiveData(Student())
    val studentsRecord: LiveData<List<Student>> = classRepository.getStudentsRecord()
    private val _isSubmit = MutableLiveData<String>()
    val isSubmit: LiveData<String> = _isSubmit

    fun submitForm() {
        // val  student = registerStudent.value
        registerStudent.value?.let { student ->
            val isReturn = when {
                student.gender.isEmpty() -> "Please select gender."
                student.firstName.isEmpty() -> "Please fill first name"
                student.middleName.isEmpty() -> "Please fill middle name"
                student.lastName.isEmpty() -> "Please fill last name"
                student.dob.isEmpty() -> "Please fill Date Of Birth."
                student.mobileNo.isEmpty() -> "Please fill Mob No."
                student.mobileNo.length < 10 -> "Please fill valid Mob No."
                student.sClass.isEmpty() -> "Please fill Class name"
                student.school.isEmpty() -> "Please fill school name"
                student.address.isEmpty() -> "Please fill address"
                else -> "Form Submitted Successfully"
            }
            _isSubmit.value = isReturn
        }
    }

    fun submitData(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            classRepository.insert(student)
        }
    }

    fun update(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            classRepository.update(student)
        }
    }

    fun insertDailyAttendance(attendance: Attendance) {
        viewModelScope.launch(Dispatchers.IO) {
            classRepository.insertDailyAttendance(attendance)
        }
    }

    fun updateAttendanceRecords(attendance: Attendance){
        viewModelScope.launch(Dispatchers.IO) {
            classRepository.updateAttendanceRecords(attendance)
        }
    }

    /*suspend fun getStudentsRecord(): LiveData<List<Student>> {
       return classRepository.getStudentsRecord()
       *//* viewModelScope.launch {
            classRepository.getStudentsRecord()
        }*//*
    }*/

    fun getAttendanceRecord(formattedDate: String): LiveData<List<Attendance>> {
       return classRepository.getAttendanceRecord(formattedDate)
    }

    fun getFilterAttendanceRecord(
        selectedFromDate: String,
        selectedToDate: String,
        isPresentAb: String
    ): LiveData<List<Attendance>> {
        return classRepository.getFilterAttendanceRecord(selectedFromDate,selectedToDate,isPresentAb)
    }

    // for this code for edit fragment
    // var selectedStudent = MutableLiveData<Student>()
    private val mutableSelectedItem = MutableLiveData<Student?>()
    val selectedItem: LiveData<Student?> get() = mutableSelectedItem

    fun setSelectedStudent(student: Student) {
        mutableSelectedItem.value = student
    }

    fun clearSelectedStudent() {
        mutableSelectedItem.value = null // This will reset the selected student data
    }
    //fun getSelectedStudent(): MutableLiveData<Student> = selectedStudent

}