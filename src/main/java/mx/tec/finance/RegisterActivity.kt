package mx.tec.finance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var nombre : EditText
    lateinit var username : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        nombre = findViewById(R.id.fullname)
        username = findViewById(R.id.username)
    }
    fun registro(view : View?){

        Firebase.auth.createUserWithEmailAndPassword(
            email.text.toString(),
            password.text.toString()
        ).addOnCompleteListener(this){

            if(it.isSuccessful){
                Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this, "Fail to register, try again", Toast.LENGTH_SHORT).show()
            }
        }
    }



}