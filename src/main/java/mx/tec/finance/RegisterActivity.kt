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
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
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
        registrarDatos(view)

        Firebase.auth.createUserWithEmailAndPassword(
            email.text.toString(),
            password.text.toString()
        ).addOnCompleteListener(this){

            if(it.isSuccessful){
                Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show()

                this.goHome(view)
            } else {
                Toast.makeText(this, "Fail to register, try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun registrarDatos(view : View?){
        data class user(
            val name: String? = nombre.text.toString(),
            val email: String? = this.email.text.toString(),
            val username: String? = this.username.text.toString()
        )
        var users= user()
        Firebase.firestore.collection("Users").document(this.email.text.toString())
            .set(users)
            .addOnSuccessListener {
                Toast.makeText(this, "wuju", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener{
                Toast.makeText(this, "Fail EFE", Toast.LENGTH_SHORT).show()

            }
    }


    public fun clickedLogIn(v : View?) {

        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }
    public fun goHome(v : View?) {

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }


}