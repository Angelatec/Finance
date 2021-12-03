package mx.tec.finance

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream

class perfilInfoActivity : AppCompatActivity() {
    private lateinit var  imageView: ImageView
    lateinit var email: String
    lateinit var name : TextView
    lateinit var emailContainer : TextView
    lateinit var usuario : TextView
    lateinit var foto : String
    lateinit var goal: TextView
    lateinit var bitmap : Bitmap
    lateinit var  image : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_info)
        name = findViewById(R.id.name1)
        emailContainer = findViewById(R.id.email1)
        goal = findViewById(R.id.goal1)
        usuario=findViewById(R.id.username1)
        imageView = findViewById(R.id.Foto1)
        email= intent.getStringExtra("email").toString()

        bitmap = imageView.drawable.toBitmap()

        var data = Firebase.firestore.collection("Users").document(email)
            .get()
            .addOnSuccessListener { document ->
                var lista= document.data?.values?.toList()

                emailContainer.setText(lista?.get(3)?.toString())
                goal.setText(lista?.get(0)?.toString())
                name.setText(lista?.get(1)?.toString())
                usuario.setText(lista?.get(4)?.toString())
                foto = lista?.get(2).toString()
                if(foto.length>10){
                    val imageBytes = Base64.decode(lista?.get(2).toString(), 0)
                    val imageBitMap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    imageView.setImageBitmap(imageBitMap)
                }else{
                    addPhoto()
                }

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
    public fun addPhoto(){

        image= BitMapToString(bitmap)
        data class user(
            val photo: String? = this.image,
        )
        var users= user()
        Firebase.firestore.collection("Users").document(email.toString())
            .set(users, SetOptions.merge())
            .addOnSuccessListener {
                // Toast.makeText(this, "Photo", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Fail EFE", Toast.LENGTH_SHORT).show()
            }
    }
    public fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    public fun edit(v : View?){
        val intent = Intent(this, PerfilActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }
    public fun back(v : View?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }
}
