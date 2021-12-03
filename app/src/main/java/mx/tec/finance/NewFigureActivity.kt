package mx.tec.finance

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
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
import java.lang.Integer.parseInt
import java.time.temporal.TemporalAmount

class NewFigureActivity : AppCompatActivity() {
    lateinit var email: String
    lateinit var radioGroup: RadioGroup
    lateinit var amount: EditText
    lateinit var category: EditText
    lateinit var description: EditText
    lateinit var radioExpense: RadioButton
    lateinit var imageView: ImageView
    lateinit var selectButton: Button
    private val pickImage = 100
    lateinit var imageUri : Uri
    lateinit var  bitmap : Bitmap
    lateinit var imageString : String

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        bitmap = result.data?.extras?.get("data") as Bitmap
        imageView.setImageBitmap(bitmap)
        imageString = BitMapToString(bitmap)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email= intent.getStringExtra("email").toString()
        setContentView(R.layout.activity_new_figure)
        radioGroup = findViewById(R.id.radioGroup)
        amount = findViewById(R.id.amoun)
        category = findViewById(R.id.categ)
        description = findViewById(R.id.descrip)
        radioExpense = findViewById(R.id.expense)
        imageView = findViewById(R.id.imageView4)
        selectButton = findViewById(R.id.button17)

        bitmap = imageView.drawable.toBitmap()
        imageString= BitMapToString(bitmap)

        selectButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data!!
            imageView.setImageURI(imageUri)
            val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            bitmap= imageBitmap
            imageString = BitMapToString(imageBitmap)

        }
    }

    public fun tomarFoto(view: View){

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePhotoIntent)

    }

    public fun create(v : View?) {

        imageString= BitMapToString(bitmap)
        data class figure(

            val cantidad: Long? = parseInt(amount.text.toString()).toLong(),
            val categoria: String? = category.text.toString(),
            val descripcion: String? = description.text.toString(),
            val foto: String? = imageString,
        )
        var figures= figure()
        var tipo=""

        if(radioGroup.checkedRadioButtonId.toString()==radioExpense.id.toString()){
            tipo="gastos"
        }else{
            tipo="ingresos"
        }

        Firebase.firestore.collection("movimientos").document(email).collection(tipo).document()
            .set(figures)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show()
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
    public fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


}