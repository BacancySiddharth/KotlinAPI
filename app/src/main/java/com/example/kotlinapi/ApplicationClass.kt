package com.example.kotlinapi

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.example.kotlinapi.db.SqliteManager
import io.realm.Realm
import kotlin.properties.Delegates

/**
 * Created by siddharth on 4/6/18.
 */
class ApplicationClass : Application() {
   open var dbHelper: SqliteManager by Delegates.notNull()
   open  var db: SQLiteDatabase by Delegates.notNull()

    init {
        instance = this
    }

    companion object {
        private var instance: ApplicationClass? = null

        fun applicationContext() : ApplicationClass {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this)

        dbHelper = SqliteManager(this)
        db = dbHelper.getWritableDatabase()

    }
}