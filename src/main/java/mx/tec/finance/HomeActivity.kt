package mx.tec.finance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    lateinit var name : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        name = findViewById(R.id.name)
        Firebase.firestore.collection("Users")
            .get()
            .addOnSuccessListener {

                for(documento in it){


                    Log.d("FIRESTORE", "${documento.id} ${documento.data}")
                }
            }
            .addOnFailureListener{
                Log.e("FIRESTORE", "error al leer gatitos: ${it.message}")
            }
    }
}
