package mx.tec.finance

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream


class PerfilActivity() : AppCompatActivity() {

    private lateinit var  imageView: ImageView
    lateinit var email: String

    lateinit var  image : String
    lateinit var name : TextView
    lateinit var emailContainer : TextView

    lateinit var usuario : EditText
    lateinit var foto : String
    lateinit var goal: TextView
    lateinit var bitmap : Bitmap
    lateinit var selectButton : Button
    private val pickImage = 100
    lateinit var imageUri : Uri

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        bitmap = result.data?.extras?.get("data") as Bitmap
        imageView.setImageBitmap(bitmap)

        image = BitMapToString(bitmap)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        name = findViewById(R.id.user_name)
        usuario=findViewById(R.id.user_user)

        imageView = findViewById(R.id.Foto1)

        email= intent.getStringExtra("email").toString()

        selectButton = findViewById(R.id.button21)

        selectButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        var data = Firebase.firestore.collection("Users").document(email)
            .get()
            .addOnSuccessListener { document ->
                var lista= document.data?.values?.toList()
                name.setText(lista?.get(1)?.toString())
                usuario.setText(lista?.get(4)?.toString())
                foto = lista?.get(2).toString()
                val imageBytes = Base64.decode(lista?.get(2).toString(), 0)
                val imageBitMap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imageView.setImageBitmap(imageBitMap)
                image= BitMapToString(imageBitMap)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

    }


    public fun updateUser(view: View?){

        image= BitMapToString(bitmap)
        data class user(
            val name: String? = this.name.text.toString(),
            val photo: String? = this.image,
            val username: String? = this.usuario.text.toString()
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
        val intent = Intent(this, perfilInfoActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data!!
            imageView.setImageURI(imageUri)
            val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            bitmap= imageBitmap
            image = BitMapToString(imageBitmap)

        }
    }
    public fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    public fun tomarFoto(view: View){

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePhotoIntent)

    }
    public fun back(v : View?) {
        val intent = Intent(this, perfilInfoActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }

}