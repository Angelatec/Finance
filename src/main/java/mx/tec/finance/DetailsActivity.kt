package mx.tec.finance

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailsActivity : AppCompatActivity() {

    lateinit var type : TextView
    lateinit var amount : TextView
    lateinit var category : TextView
    lateinit var description : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        type = findViewById(R.id.type)
        amount = findViewById(R.id.amount)
        category = findViewById(R.id.category)
        description = findViewById(R.id.description)


        //accesamos a movimientos luego al usuario y finalmente a sus movimientos
        var data =Firebase.firestore.collection("movimientos").document("angela.itesm.com").collection("movimientos")
            .get()
            .addOnSuccessListener { documents ->
                // recorremos cada movimiento
                for (document in documents) {

                    //document.data.
                    //Log.d(TAG, "${document.id} => ${document.data().categoria}") //para accesar usamos data().loquequieras
                    //type.text = document.data().tipo
                    //amount.text = document.data().importe
                    //category.text = document.data().tipo
                    //description.text = document.data().comentarios
                    type.setText(document.data.values.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}