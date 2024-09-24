package com.example.attendance.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.attendance.model.Attendance
import com.example.attendance.model.Student

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: Student)

    @Query("SELECT * FROM tbl_student")
    fun getStudentsRecord(): LiveData<List<Student>>

    @Query("SELECT * FROM Attendance where Attendance.date==:date")
    fun getAttendanceRecord(date:String): LiveData<List<Attendance>>

    @Update
    suspend fun update(student: Student)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyAttendance(attendance: Attendance)

    @Update
    suspend fun updateAttendanceRecords(attendance: Attendance)

    /*@Query("SELECT * FROM Attendance where date BETWEEN :selectedFromDate AND :selectedToDate")
    fun getFilterAttendanceRecord(selectedFromDate: String, selectedToDate: String): LiveData<List<Attendance>>*/
    @RawQuery(observedEntities = [Attendance::class])
    //fun getFilterAttendanceRecord(selectedFromDate: String, selectedToDate: String): LiveData<List<Attendance>>
    fun getFilterAttendanceRecord(query: SupportSQLiteQuery):  LiveData<List<Attendance>>

}