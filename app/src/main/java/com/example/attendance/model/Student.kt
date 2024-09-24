package com.example.attendance.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tbl_student")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var firstName: String = "",
    var middleName: String = "",
    var lastName: String = "",
    var gender: String = "",
    var dob: String = "",
    var address: String = "",
    var sClass: String = "",
    var mobileNo: String = "",
    var school: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun toString(): String {
        return "$firstName $middleName $lastName"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeInt(id)
        parcel.writeString(firstName)
        parcel.writeString(middleName)
        parcel.writeString(lastName)
        parcel.writeString(gender)
        parcel.writeString(dob)
        parcel.writeString(address)
        parcel.writeString(sClass)
        parcel.writeString(mobileNo)
        parcel.writeString(school)
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}

