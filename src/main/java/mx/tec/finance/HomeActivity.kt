package mx.tec.finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    lateinit var name : TextView
    lateinit var buttonIngreso : Button
    lateinit var buttonGasto : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        name = findViewById(R.id.name)
        buttonIngreso = findViewById(R.id.buttonI)
        buttonGasto = findViewById(R.id.buttonG)

    }
    public fun details(v : View?) {
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }
}
