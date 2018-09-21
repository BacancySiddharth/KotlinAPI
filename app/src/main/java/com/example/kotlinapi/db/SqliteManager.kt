package com.example.kotlinapi.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log.e
import com.example.kotlinapi.model.RSS
import com.example.kotlinapi.model.RssFeed

/**
 * Created by siddharth on 5/6/18.
 */


class SqliteManager(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME,
                null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLENAME + "("
                + RSSFEED_title + " TEXT PRIMARY KEY," +
                RSSFEED_description + " TEXT," +
                RSSFEED_link + " TEXT," +
                RSSFEED_image + " TEXT," +
                RSSFEED_category + " TEXT," +
                RSSFEED_pubDate + " TEXT)")

        val CREATE_RSS_TABLE = ("CREATE TABLE " +
                RSS + "("
                + RSS_LINK + " TEXT PRIMARY KEY," +
                RSS_NAME + " TEXT)")

        db.execSQL(CREATE_PRODUCTS_TABLE)
        db.execSQL("DROP TABLE IF EXISTS " + RSS)
        db.execSQL(CREATE_RSS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME)
        db.execSQL("DROP TABLE IF EXISTS " + RSS)

        onCreate(db)
    }

    companion object {
        private val DATABASE_VERSION = 8
        private val DATABASE_NAME = "Sqlitedb.db"
        val TABLENAME = "RSSFEED"
        private val RSSFEED_title = "RSSFEED_title"
        private val RSSFEED_description = "RSSFEED_description"
        private val RSSFEED_link = "RSSFEED_link"
        private val RSSFEED_image = "RSSFEED_image"
        private val RSSFEED_pubDate = "RSSFEED_pubDate"
        private val RSSFEED_category = "RSSFEED_category"

        val RSS = "RSS"
        private val RSS_LINK = "RSS_LINK"
        private val RSS_NAME = "RSS_NAME"
    }

    fun addProduct(product: RssFeed) {

        val values = ContentValues()
        values.put(RSSFEED_title, product.title)
        values.put(RSSFEED_description, product.description)
        values.put(RSSFEED_link, product.link)
        values.put(RSSFEED_image, product.image)
        values.put(RSSFEED_pubDate, product.pubDate)
        values.put(RSSFEED_category, product.pubDate)

        val db = this.writableDatabase

        db.insert(TABLENAME, null, values)
        db.close()
    }

    fun findProductlist(productname: String): ArrayList<RssFeed> {

        var query = "SELECT * FROM $TABLENAME"

        if (productname.isNullOrBlank()) {
            query = "SELECT * FROM $TABLENAME "
        } else {
            query = "SELECT * FROM $TABLENAME WHERE $RSSFEED_category =  \"$productname\""
        }

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var product: ArrayList<RssFeed>? = ArrayList()

        /*if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val title = cursor.getString(0)
            val description = cursor.getString(1)
            val link = cursor.getString(2)
            val image = cursor.getString(3)
            val pubDate = cursor.getString(4)
            product = RssFeed(title, description, link, image, pubDate)
            cursor.close()
        }
*/

        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    val title = cursor.getString(0)
                    val description = cursor.getString(1)
                    val link = cursor.getString(2)
                    val image = cursor.getString(3)
                    val pubDate = cursor.getString(4)
                    val category = cursor.getString(5)
                    product?.add(0, RssFeed(title, description, link, image, pubDate,category))
                } while (cursor.moveToNext())
            }

        } finally {
            try {
                cursor.close()
            } catch (ignore: Exception) {
                ignore.printStackTrace()
            }

        }


        db.close()
        return product!!
    }

    fun addRssData(name: String, link: String) {
        val values = ContentValues()
        values.put(RSS_NAME, name)
        values.put(RSS_LINK, link)
        val db = this.writableDatabase
        db.insert(RSS, null, values)
        Log@ e("db", "RSS ADD SUCCESSFULL")
        db.close()
    }

    fun loadAllRssFeed(name: String): ArrayList<RSS> {
        var query=""
        if (name.equals("")) {
            query = "SELECT * FROM $RSS"
        } else {
            query = "SELECT * FROM $RSS WHERE $RSS_NAME =  \"$name\""

        }
/*
        if (productname.isNullOrBlank()) {
            query = "SELECT * FROM $RSS "
        } else {
            query = "SELECT * FROM $RSS"
        }*/

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var rss: ArrayList<RSS>? = ArrayList()

        Log@ e("db", "RSS GET ")

        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    val link = cursor?.getString(0)
                    val title = cursor?.getString(1)
                    Log@ e("db", "RSS GET " + title)
                    rss?.add(RSS(title, link))
                } while (cursor.moveToNext())
            }

        } finally {
            try {
                cursor.close()
            } catch (ignore: Exception) {
                ignore.printStackTrace()
            }

        }


        db.close()
        return rss!!
    }

}