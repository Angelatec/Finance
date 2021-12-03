package mx.tec.finance

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.parseInt

class DetailsActivity : AppCompatActivity() {
    lateinit var email: String
    lateinit var type : TextView
    lateinit var amount : TextView
    lateinit var category : TextView
    lateinit var description : TextView
    lateinit var imageString: String
    lateinit var imageView: ImageView
    var id=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        id= intent.getStringExtra("id").toString()
        type = findViewById(R.id.type)
        amount = findViewById(R.id.amount)
        category = findViewById(R.id.category)
        description = findViewById(R.id.description)
        email= intent.getStringExtra("email").toString()
        type.text=intent.getStringExtra("type")
        description.text=intent.getStringExtra("description")
        category.text=intent.getStringExtra("category")
        amount.text=intent.getStringExtra("amount")
        imageString = intent.getStringExtra("imageString").toString()
        imageView = findViewById(R.id.imageView5)
        val imageBytes = Base64.decode(imageString, 0)
        val imageBitMap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        imageView.setImageBitmap(imageBitMap)
    }

    public fun edit(v : View?) {
        data class figure(
            val cantidad: Long? = parseInt(amount.text.toString()).toLong(),
            val categoria: String? = category.text.toString(),
            val descripcion: String? = description.text.toString(),
        )
        var figures = figure()
        var tipo = "gastos"
        if (type.text.toString() == "ENTRY") {
            tipo = "ingresos"
        }
        Firebase.firestore.collection("movimientos").document(email).collection(tipo).document(id)
            .set(figures, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "SUCCESSFULLY UPDATED", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Fail EFE", Toast.LENGTH_SHORT).show()
            }
    }
    public fun delete(v : View?) {
        var tipo = "gastos"
        if (type.text.toString() == "ENTRY") {
            tipo = "ingresos"
        }
        Firebase.firestore.collection("movimientos").document(email).collection(tipo).document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "SUCCESSFULLY DELETED", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Fail EFE", Toast.LENGTH_SHORT).show()
            }
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }
    public fun back(v : View?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }
}