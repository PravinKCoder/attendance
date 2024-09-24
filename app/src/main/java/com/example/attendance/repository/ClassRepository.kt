package com.example.attendance.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.attendance.database.ClassDatabase
import com.example.attendance.model.Attendance
import com.example.attendance.model.Student

class ClassRepository(private val db: ClassDatabase) {
    suspend fun insert(student: Student) {
        db.getStudentDao().insert(student)

    }

    suspend fun update(student: Student) {
        db.getStudentDao().update(student)
    }


    fun getStudentsRecord(): LiveData<List<Student>> {
        return db.getStudentDao().getStudentsRecord()
    }

    suspend fun insertDailyAttendance(attendance: Attendance) {
        db.getStudentDao().insertDailyAttendance(attendance)
    }


    fun getAttendanceRecord(formattedDate: String): LiveData<List<Attendance>> {
        return db.getStudentDao().getAttendanceRecord(formattedDate)
    }

    suspend fun updateAttendanceRecords(attendance: Attendance) {
        db.getStudentDao().updateAttendanceRecords(attendance)
    }

    fun getFilterAttendanceRecord(
        selectedFromDate: String,
        selectedToDate: String,
        isPresentAb: String
    ): LiveData<List<Attendance>> {
        //var where= "where date BETWEEN :$selectedFromDate AND :$selectedToDate"

        return db.getStudentDao().getFilterAttendanceRecord(
            getDynamicQuery(
                selectedFromDate,
                selectedToDate,
                isPresentAb
            )
        )
    }

    private fun getDynamicQuery(
        fromDate: String,
        toDate: String,
        isPresentAb: String
    ): SupportSQLiteQuery {
        var baseQuery = "SELECT * FROM Attendance WHERE date BETWEEN ? AND ?"
        val args = mutableListOf<Any>(fromDate, toDate)
        if (isPresentAb != "") {
            baseQuery += " AND attendance =:$isPresentAb"
            args.add(isPresentAb)
        }

        return SimpleSQLiteQuery(baseQuery, args.toTypedArray())
    }
}