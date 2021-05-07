package com.plete.tournal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class dbHandler(context: Context): SQLiteOpenHelper
    (context, databaseName, null, databaseVersion) {

    companion object{
        private val databaseVersion = 1
        private val databaseName = "tournal"

        private val tableName = "tuornalTable"

        private val keyId = "id"
        private val keyDesc = "desc"
        private val keyDate = "date"
        private val keyLoc = "loc"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("""
            create table $tableName ($keyId integer primary key, $keyDesc text,
            $keyDate text, $keyLoc text)
        """.trimIndent())
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists $tableName")
        onCreate(db)
    }

    fun save(DB: dbModel): Long{
        val db = this.writableDatabase
        val contentvalues = ContentValues()

        contentvalues.put(keyDesc, DB.desc)
        contentvalues.put(keyDate, DB.date)
        contentvalues.put(keyLoc, DB.loc)

        val success = db.insert(tableName, null, contentvalues)
        db.close()
        return success
    }

    fun viewDB(): ArrayList<dbModel>{
        val dbList: ArrayList<dbModel> = ArrayList<dbModel>()
        val selectQuery = "select * from $tableName"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var desc: String
        var date: String
        var loc: String

        if (cursor.moveToFirst()){
            do {
                id = cursor.getInt((cursor.getColumnIndex(keyId)))
                desc = cursor.getString((cursor.getColumnIndex(keyDesc)))
                date = cursor.getString(cursor.getColumnIndex(keyDate))
                loc = cursor.getString((cursor.getColumnIndex(keyLoc)))

                val dB = dbModel(id, desc, date, loc)
                dbList.add(dB)
            } while (cursor.moveToNext())
        }
        return dbList
    }
}