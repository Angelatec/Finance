package mx.tec.finance

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() , View.OnClickListener{
    lateinit var name : TextView
    lateinit var buttonIngreso : Button
    lateinit var buttonGasto : Button
    lateinit var email: String
    lateinit var rvexpenditures: RecyclerView
    lateinit var rventries: RecyclerView
    lateinit var adapterExp: Adapter
    lateinit var adapterEnt: Adapter
    private var arrayGastos: ArrayList<ArrayList<String>> = ArrayList()
    private var arrayIngresos: ArrayList<ArrayList<String>> = ArrayList()
    lateinit var image : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        email= intent.getStringExtra("email").toString()


        rvexpenditures = findViewById(R.id.rvexpenditures)
        rventries= findViewById(R.id.rventries)

        var aux=Firebase.firestore.collection("movimientos").document(email).collection("gastos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var figure:ArrayList<String> = ArrayList()
                    figure.add(document.id)
                    figure.add(document.data?.values?.elementAt(0).toString())
                    figure.add(document.data?.values?.elementAt(1).toString())
                    figure.add(document.data?.values?.elementAt(2).toString())
                    figure.add(document.data?.values?.elementAt(3).toString())
                    arrayGastos.add(figure)
                }
                adapterExp= Adapter(arrayGastos,this)
                var llm = LinearLayoutManager(this)
                llm.orientation = LinearLayoutManager.VERTICAL

                rvexpenditures.layoutManager = llm
                rvexpenditures.adapter = adapterExp
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
        var aux1=Firebase.firestore.collection("movimientos").document(email).collection("ingresos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var figure:ArrayList<String> = ArrayList()
                    figure.add(document.id)
                    figure.add(document.data?.values?.elementAt(0).toString())
                    figure.add(document.data?.values?.elementAt(1).toString())
                    figure.add(document.data?.values?.elementAt(2).toString())
                    figure.add(document.data?.values?.elementAt(3).toString())
                    arrayIngresos.add(figure)
                }
                adapterEnt= Adapter(arrayIngresos,this)
                var llm = LinearLayoutManager(this)
                llm.orientation = LinearLayoutManager.VERTICAL

                rventries.layoutManager = llm
                rventries.adapter = adapterEnt
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    public fun add(v : View?) {
        val intent = Intent(this, NewFigureActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }
    public fun goal(v : View?) {
        val intent = Intent(this, GoalActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }
    public fun graph(v : View?) {
        val intent = Intent(this, ChartsActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    override fun onClick(row: View) {
        val parent = row.parent
        if((parent as RecyclerView).id==R.id.rvexpenditures){
            val position = rvexpenditures.getChildLayoutPosition(row)
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("id", arrayGastos[position][0])
            intent.putExtra("type", "EXPENDITURE")
            intent.putExtra("description", arrayGastos[position][1])
            intent.putExtra("imageString", arrayGastos[position][2])
            intent.putExtra("category", arrayGastos[position][3])
            intent.putExtra("amount",arrayGastos[position][4])
            startActivity(intent)
        }else {
            val position = rventries.getChildLayoutPosition(row)
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("id", arrayIngresos[position][0])
            intent.putExtra("type", "ENTRY")
            intent.putExtra("description", arrayIngresos[position][1])
            intent.putExtra("imageString", arrayIngresos[position][2])
            intent.putExtra("category", arrayIngresos[position][3])
            intent.putExtra("amount",arrayIngresos[position][4])
            startActivity(intent)
        }
    }
    public fun perfil(v : View?){
        val intent = Intent(this, perfilInfoActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }


}
