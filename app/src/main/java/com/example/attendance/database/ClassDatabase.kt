package com.example.attendance.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.attendance.model.Attendance

import com.example.attendance.model.Student

@Database(entities = [Student::class,Attendance::class], version = 1, exportSchema = false)
abstract class ClassDatabase() : RoomDatabase() {
    abstract fun getStudentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: ClassDatabase? = null
        fun getInstance(context: Context): ClassDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context, ClassDatabase::class.java, "classDB").build()
                }
            }
            return INSTANCE!!
        }

    }

    /* companion object {
         @Volatile
         var INSTANCE: ClassDatabase? = null
         fun getDatabaseInstance(context: Context): ClassDatabase {
             val tempInstance = INSTANCE
             if (tempInstance != null) {
                 return tempInstance
             } else {
                 val rmDB = Room.databaseBuilder(context, ClassDatabase::class.java, "classDB").build()
                 INSTANCE = rmDB
                 return rmDB
             }
         }
     }
 */
}