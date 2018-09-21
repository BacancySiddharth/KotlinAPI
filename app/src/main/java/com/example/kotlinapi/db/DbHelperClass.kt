package com.example.kotlinapi.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.kotlinapi.model.RssFeed
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable

/**
 * Created by siddharth on 5/6/18.
 */
class DbHelperClass(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "kotlinDB") {

    var tablename: String = "RSSFEED"

    companion object {
        private var instance: DbHelperClass? = null

        private val RSSFEED_title = "RSSFEED_title"
        private val RSSFEED_description = "RSSFEED_description"
        private val RSSFEED_link = "RSSFEED_link"
        private val RSSFEED_image = "RSSFEED_image"
        private val RSSFEED_pubDate = "RSSFEED_pubDate"

        @Synchronized
        fun getInstance(ctx: Context): DbHelperClass {
            if (instance == null) {
                instance = DbHelperClass(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.createTable(tablename, true,
                "title" to TEXT + PRIMARY_KEY,
                "description" to TEXT,
                "link" to TEXT,
                "image" to TEXT,
                "pubDate" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + tablename)
        onCreate(db)
    }

    fun addProduct(product: RssFeed) {

        val values = ContentValues()
        values.put(RSSFEED_title, product.title)
        values.put(RSSFEED_description, product.description)
        values.put(RSSFEED_link, product.link)
        values.put(RSSFEED_image, product.image)
        values.put(RSSFEED_pubDate, product.pubDate)

        val db = this.writableDatabase

        db.insert(tablename, null, values)
        db.close()
    }

}