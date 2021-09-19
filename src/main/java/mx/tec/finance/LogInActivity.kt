package mx.tec.finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {

    lateinit var email : EditText
    lateinit var password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
    }



    fun login(view : View?){

        Firebase.auth.signInWithEmailAndPassword(
            email.text.toString(),
            password.text.toString()
        ).addOnCompleteListener(this){

            if(it.isSuccessful) {
                Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show()
                this.goHome(view)
            }
            else
                Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show()
        }
    }

    public fun clickedRegister(v : View?) {

        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    public fun goHome(v : View?) {

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}