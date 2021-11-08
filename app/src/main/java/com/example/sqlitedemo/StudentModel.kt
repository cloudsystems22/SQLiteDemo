package com.example.sqlitedemo

import java.util.*

data class StudentModel (
    var Id:Int = getAutoId(),
    var Name:String = "",
    var Email:String = ""
)
{
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}