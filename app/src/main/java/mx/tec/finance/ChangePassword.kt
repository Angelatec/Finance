package mx.tec.finance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore

class ChangePassword : AppCompatActivity() {
    lateinit var email : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        email = findViewById(R.id.change_input)
    }

    public fun submit(v: View?){
        Firebase.auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener(this) {
                if(it.isSuccessful){
                    Toast.makeText(this, "Successfully Send", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                }
            }
    }
}