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
        if(intent.getStringExtra("name")=="gasto1") {
            type.setText("Expense")
            var data = Firebase.firestore.collection("movimientos").document("angela.itesm.com")
                .collection("gastos").document("SXgYJPnCid0ekqslBQx8")
                .get()
                .addOnSuccessListener { document ->
                    var lista= document.data?.values?.toList()
                    amount.setText(lista?.get(2)?.toString())
                    category.setText(lista?.get(1)?.toString())
                    description.setText(lista?.get(0)?.toString())

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "AAError getting documents: ", exception)
                }
        }else if(intent.getStringExtra("name")=="gasto2") {
            type.setText("Expense")
            var data = Firebase.firestore.collection("movimientos").document("angela.itesm.com")
                .collection("gastos").document("nUOd2yje04VeEXRe9QA5")
                .get()
                .addOnSuccessListener { document ->
                    var lista = document.data?.values?.toList()
                    amount.setText(lista?.get(2)?.toString())
                    category.setText(lista?.get(1)?.toString())
                    description.setText(lista?.get(0)?.toString())

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "AAError getting documents: ", exception)
                }
        }
        else {
            type.setText("Income")
            var data = Firebase.firestore.collection("movimientos").document("angela.itesm.com")
                .collection("ingresos").document("3TlwPqd80VQ48sPkfBMX")
                .get()
                .addOnSuccessListener { document ->
                    var lista = document.data?.values?.toList()
                    amount.setText(lista?.get(2)?.toString())
                    category.setText(lista?.get(1)?.toString())
                    description.setText(lista?.get(0)?.toString())

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }
}