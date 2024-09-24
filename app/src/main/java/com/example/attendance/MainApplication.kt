package com.example.attendance

import android.app.Application
import com.example.attendance.database.ClassDatabase
import com.example.attendance.repository.ClassRepository

class MainApplication : Application() {
    lateinit var classRepository: ClassRepository
    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val quoteDatabase = ClassDatabase.getInstance(applicationContext)
        classRepository = ClassRepository(quoteDatabase)

    }
}