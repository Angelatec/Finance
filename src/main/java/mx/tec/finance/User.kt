package mx.tec.finance

import android.os.Bundle
import android.widget.EditText

class User(email:String,password:String,name:String,username:String) {
    lateinit var email : String
    lateinit var password : String
    lateinit var name : String
    lateinit var username : String

    fun onCreate(email:String,password:String,name:String,username:String) {

        this.email = email
        this.password =password
        this.name = name
        this.username = username
    }



}