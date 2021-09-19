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

        // 1ero - crear un hashmap
        val gatito = hashMapOf(
            "nombre" to nombre.text.toString(),
            "edad" to username.text.toString()
        )

        Firebase.firestore.collection("gatitos")
            .add(gatito)
            .addOnSuccessListener {
                Toast.makeText(this, "wuju", Toast.LENGTH_SHORT).show()

                Log.d("FIREBASE", "id: ${it.id}")
            }
            .addOnFailureListener{
                Toast.makeText(this, "Fail EFE", Toast.LENGTH_SHORT).show()

                Log.e("FIREBASE", "excepcion: ${it.message}")
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