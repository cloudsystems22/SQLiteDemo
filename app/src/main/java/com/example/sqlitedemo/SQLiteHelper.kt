package com.example.sqlitedemo

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.lang.Exception

class SQLiteHelper(context:Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "student.db"
        private const val TBL_STUDENT = "tbl_student"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTblStudant = ("CREATE TABLE $TBL_STUDENT" +
                "($ID INTEGER PRIMARY KEY," +
                "$NAME TEXT," +
                "$EMAIL TEXT)")
        db?.execSQL(createTblStudant)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT")
        onCreate(db)
    }

    fun insertStudent(std:StudentModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, std.Id)
        contentValues.put(NAME, std.Name)
        contentValues.put(EMAIL, std.Email)

        val success = db.insert(TBL_STUDENT, null, contentValues)
        db.close()

        return success

    }

    fun getAllStudent():ArrayList<StudentModel>{
        val stdList: ArrayList<StudentModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_STUDENT"
        val db = this.readableDatabase

        val cursor:Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e:Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id:Int
        var name:String
        var email:String

        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                email = cursor.getString(cursor.getColumnIndexOrThrow("email"))

                val std = StudentModel(id, name, email)
                stdList.add(std)
            } while(cursor.moveToNext())
        }

        return stdList
    }

    fun updateStudent(std:StudentModel):Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, std.Id)
        contentValues.put(NAME, std.Name)
        contentValues.put(EMAIL, std.Email)

        val success = db.update(TBL_STUDENT, contentValues, "id=${std.Id}", null)
        db.close()

        return success

    }

    fun deleteStudent(id:Int):Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(TBL_STUDENT, "id=$id", null)
        db.close()
        return success
    }

}