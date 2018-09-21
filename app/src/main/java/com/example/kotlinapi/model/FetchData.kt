package com.example.kotlinapi.model

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by siddharth on 4/6/18.
 */
open class Student(
        @PrimaryKey open var _ID: Int = 0,
        open var stuSex: Boolean = false,
        open var firstName: String = "",
        open var lastName: String = "",
        open var stuClass: Int = 0
) : RealmObject(), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    fun copy(
            _ID: Int = this._ID,
            stuSex: Boolean = this.stuSex,
            firstName: String = this.firstName,
            lastName: String = this.lastName,
            stuClass: Int = this.stuClass) = Student(_ID, stuSex, firstName, lastName, stuClass)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(_ID)
        parcel.writeByte(if (stuSex) 1 else 0)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeInt(stuClass)
    }

    override fun describeContents(): Int {
        return 0
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