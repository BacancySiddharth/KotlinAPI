package com.example.kotlinapi.model

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by siddharth on 1/6/18.
 */
open class RssFeedRealm(
        @PrimaryKey open var title: String = "", open var description: String = "", open var link: String = "", open var image: String = "", open var pubDate: String = ""
) : RealmObject() {

    /*constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }*/

    /*    fun copy(
                title: String? = this.title,
                description: String? = this.description,
                link: String? = this.link,
                image: String? = this.image,
                pubDate: String? = this.pubDate) = RssFeedRealm(title, description, link, image, pubDate)

       companion object CREATOR : Parcelable.Creator<Student> {
           override fun createFromParcel(parcel: Parcel): Student {
               return Student(parcel)
           }

           override fun newArray(size: Int): Array<Student?> {
               return arrayOfNulls(size)
           }
       }
   */
}