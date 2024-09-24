package com.example.attendance.model

import androidx.room.Entity

/*@Entity(
    "tbl_attendance",
    indices = [Index(value = ["studentId", "date"], unique = true)]
)*/
@Entity(primaryKeys = ["studentId", "date"])
data class Attendance(
   // @PrimaryKey(autoGenerate = true)
   // val id: Int = 0,
    val studentId: String = "",
    val studentName: String="",
    val month: String = "",
    val year: String = "",
    val date: String = "",
    val attendance: String = ""
)