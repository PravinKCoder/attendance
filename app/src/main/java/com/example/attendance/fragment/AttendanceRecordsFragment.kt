package com.example.attendance.fragment

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.attendance.MainApplication
import com.example.attendance.R
import com.example.attendance.adapter.DailyAttendanceAdapter
import com.example.attendance.databinding.FragmentAttendanceRecordsBinding
import com.example.attendance.model.Attendance
import com.example.attendance.viewmodel.MainViewModel
import com.example.attendance.viewmodel.MainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AttendanceRecordsFragment : Fragment(), DailyAttendanceAdapter.OnClickAttendanceListener {
    private lateinit var binding: FragmentAttendanceRecordsBinding
    private lateinit var viewModel: MainViewModel
    private var isRecord = false;
    private var attendanceList = ArrayList<Attendance>()
    private lateinit var adapter: DailyAttendanceAdapter
    private var fromDateInMillis: String? = null
    private var fromDate: Calendar? = null
    private var toDate: Calendar? = null
    private var isPresentAb: String = ""
    private var selectedFromDate: String = ""
    private var selectedToDate: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceRecordsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialize() {

        (activity as AppCompatActivity)?.supportActionBar?.title = "Attendance Records"
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val repository = (requireActivity().application as MainApplication).classRepository
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatterDate = currentDate.format(formatter)

        viewModel.getAttendanceRecord(formatterDate).observe(viewLifecycleOwner) { attendance ->
            if (!isRecord) {
                isRecord = true
                attendanceList = attendance as ArrayList
                adapter = DailyAttendanceAdapter(attendance, this)
                binding.recyclerViewAttendance.adapter = adapter
            }
        }

        binding.searchAttendanceRecord.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterRecord(newText)
                return true
            }

        })

        binding.ivFilter.setOnClickListener {
            bottomDialog()
        }

    }

    private fun bottomDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView =
            LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetView.findViewById<TextView>(R.id.tvFromDate).setOnClickListener {
            //showDatePicker(it.findViewById(R.id.tvFromDate), "From")
            showFromDatePicker(it.findViewById(R.id.tvFromDate))
        }
        bottomSheetView.findViewById<TextView>(R.id.tvToDate).setOnClickListener {
            if (fromDateInMillis != null)
            //showDatePicker(it.findViewById(R.id.tvToDate), "To")
                showToDatePicker(it.findViewById(R.id.tvToDate))
            else
                Toast.makeText(requireContext(), "Please select From Date", Toast.LENGTH_SHORT)
                    .show()
        }
        val rbPresent = bottomSheetView.findViewById<RadioButton>(R.id.rbPresent)
        val rbAbsent = bottomSheetView.findViewById<RadioButton>(R.id.rbAbsent)

        rbPresent.setOnClickListener {
            isPresentAb = "P"
        }
        rbAbsent.setOnClickListener {
            isPresentAb = "A"
        }
        bottomSheetView.findViewById<Button>(R.id.btnApply).setOnClickListener {
            isPresentAb = if (rbPresent.isChecked || rbAbsent.isChecked) isPresentAb else ""

            viewModel.getFilterAttendanceRecord(selectedFromDate, selectedToDate, isPresentAb)
                .observe(viewLifecycleOwner) { attendance ->
                    binding.recyclerViewAttendance.adapter =
                        DailyAttendanceAdapter(attendance, this)
                    Log.d("TAG", "bottomDialog: ${attendance.size}")
                }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }


    private fun filterRecord(newText: String?) {
        newText?.let {
            val filterList = ArrayList<Attendance>()
            for (name in attendanceList) {
                if (name.studentName.lowercase().contains(newText)) {
                    filterList.add(name)
                }
            }

            if (filterList.isEmpty()) {
                Toast.makeText(requireContext(), "No Record Found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilterList(filterList)
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
        viewModel.updateAttendanceRecords(attendance)
    }

    private fun showFromDatePicker(tvFromDate: TextView) {
        // Build and show the "From Date" picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create "From Date" DatePickerDialog
        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                fromDate = Calendar.getInstance()
                fromDate!!.set(selectedYear, selectedMonth, selectedDay)

                selectedFromDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                tvFromDate.text = selectedFromDate
                fromDateInMillis = selectedFromDate
                // Handle the selected "From Date" (e.g., display it or use it for your logic)
                // Now show the "To Date" picker
            }, year, month, day)

        // Set the minimum date to today's date for the "From Date" picker
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun showToDatePicker(tvToDate: TextView) {
        // Ensure a "from date" is selected before showing the "to date" picker
        if (fromDate == null) return

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create "To Date" DatePickerDialog
        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                toDate = Calendar.getInstance()
                toDate!!.set(selectedYear, selectedMonth, selectedDay)

                selectedToDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                tvToDate.text = selectedToDate
                // Handle the selected "To Date" (e.g., display it or use it for your logic)

            }, year, month, day)

        // Set the minimum date for the "To Date" picker to the selected "From Date"
        datePickerDialog.datePicker.minDate = fromDate!!.timeInMillis

        datePickerDialog.show()
    }

    /* private fun showFromDatePicker(tvFromDate: TextView) {
           // Build and show the "From Date" picker
           val fromDatePicker = MaterialDatePicker.Builder.datePicker()
               .setTitleText("Select From Date")
               .build()

           fromDatePicker.addOnPositiveButtonClickListener { selection ->
               val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
               val selectedDate = sdf.format(Date(selection))

               // Set the selected date to the TextView and store it
               tvFromDate.text = selectedDate
               fromDateInMillis = selection // Save the selected date in milliseconds
           }

           fromDatePicker.show(requireActivity().supportFragmentManager, "FROM_DATE_PICKER")
       }

       private fun showToDatePicker(tvToDate: TextView) {
           // Ensure a "from date" is selected before showing the "to date" picker
           if (fromDateInMillis != null) {
               // Build the "To Date" picker with a constraint based on the "from date"
               val constraintsBuilder = CalendarConstraints.Builder()
                   .setStart(fromDateInMillis!!)  // Set the minimum selectable date to the "from date"

               val toDatePicker = MaterialDatePicker.Builder.datePicker()
                   .setTitleText("Select To Date")
                   .setCalendarConstraints(constraintsBuilder.build())  // Apply the date constraint
                   .build()

               toDatePicker.addOnPositiveButtonClickListener { selection ->
                   val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                   val selectedDate = sdf.format(Date(selection))

                   // Set the selected date to the TextView
                   tvToDate.text = selectedDate
               }

               toDatePicker.show(requireActivity().supportFragmentManager, "TO_DATE_PICKER")
           } else {
               // Handle the case where the user tries to select "To Date" without selecting "From Date"
               Toast.makeText(requireContext(), "Please select From Date", Toast.LENGTH_SHORT)
                   .show()
           }
       }
   */
    private fun showDatePicker(tvToDate: TextView, type: String) {
        val constraintsBuilder = CalendarConstraints.Builder()
        /*  if (type == "From")
              constraintsBuilder.setValidator(DateValidatorPointBackward.now())
          else
              fromDateInMillis?.let { constraintsBuilder.setStart(it) }*/
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date of Birth")
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val selectedDate = sdf.format(Date(selection))
            //fromDateInMillis = selection
            if (type == "From") tvToDate.text = selectedDate
            else tvToDate.text = selectedDate

        }
        datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
    }

}