package mx.tec.finance

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.parseInt


class GoalActivity : AppCompatActivity() {
    lateinit var email: String
    var intGoal: Long=0
    var intExpenses: Long=0
    var intEntries: Long=0
    var intBalance: Long=0
    var intDifference: Long=0
    lateinit var goal : TextView
    lateinit var expenses : TextView
    lateinit var entries : TextView
    lateinit var balance : TextView
    lateinit var difference : TextView
    lateinit var update : Button
    lateinit var newGoal : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)
        email= intent.getStringExtra("email").toString()
        goal = findViewById(R.id.goal)
        expenses = findViewById(R.id.expenses)
        entries = findViewById(R.id.entries)
        balance = findViewById(R.id.balance)
        difference = findViewById(R.id.difference)
        update = findViewById(R.id.update)
        newGoal= findViewById(R.id.newGoal)
        difference.setTextColor(Color.BLUE)

        var data = Firebase.firestore.collection("Users").document(email).get()
            .addOnSuccessListener { document ->

                intGoal= document.data?.values?.elementAt(0) as Long
                goal.setText(intGoal.toString())
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
        //data = Firebase.firestore.collection("movimientos").document(email).collection("gastos")
        var aux=Firebase.firestore.collection("movimientos").document(email).collection("gastos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    intExpenses+= document.data?.values?.elementAt(3) as Long
                }
                expenses.text=intExpenses.toString()
                next()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }





    }
    public fun updateGoal(v : View?) {
        goal.text=newGoal.text
        intGoal= parseInt(newGoal.text.toString()).toLong()
        intDifference= intGoal.toInt()-intBalance
        difference.text= intDifference.toString()
        calculateGoal()
        data class user(
            val goal: Long = intGoal
        )
        var users= user()
        Firebase.firestore.collection("Users").document(email.toString())
            .set(users, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "SUCCESSFULLY UPDATED", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Fail EFE", Toast.LENGTH_SHORT).show()
            }
        newGoal.text=null
    }
    fun next(){
        var aux1=Firebase.firestore.collection("movimientos").document(email).collection("ingresos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    intEntries+= document.data?.values?.elementAt(3) as Long
                }
                entries.text=intEntries.toString()
                intBalance= intEntries-intExpenses
                balance.text=intBalance.toString()
                intDifference= intGoal-intBalance
                difference.text= intDifference.toString()
                calculateGoal()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
    fun calculateGoal(){
        if(intDifference<(intGoal/4)){
            difference.setTextColor(Color.GREEN)
        }else if(intDifference<(intGoal/2)) {
            difference.setTextColor(Color.YELLOW)
        }else{
            difference.setTextColor(Color.RED)
        }
    }
    public fun back(v : View?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }
}