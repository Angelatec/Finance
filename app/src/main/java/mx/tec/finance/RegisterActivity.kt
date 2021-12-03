package mx.tec.finance
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import java.io.ByteArrayOutputStream
import java.util.Base64.getEncoder
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import android.net.Uri
import android.widget.Button
import androidx.core.graphics.drawable.toBitmap


enum class ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}

class RegisterActivity : AppCompatActivity() {

    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var nombre : EditText
    lateinit var username : EditText
    lateinit var  image : Bitmap
    var imageString = "none"
    lateinit var selectButton: Button
    private val pickImage = 100
    lateinit var imageUri : Uri
    private lateinit var  imageView: ImageView



    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        image = result.data?.extras?.get("data") as Bitmap
        imageView.setImageBitmap(image)
        imageString = BitMapToString(image)


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        nombre = findViewById(R.id.fullname)
        username = findViewById(R.id.username)
        imageView = findViewById(R.id.TakeFoto)

        selectButton = findViewById(R.id.button18)
        imageString= BitMapToString(imageView.drawable.toBitmap())
        image = imageView.drawable.toBitmap()
        selectButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }



    public fun tomarFoto(view: View){

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePhotoIntent)

    }


    fun registro(view : View?){
        registrarDatos(view)
        imageString = BitMapToString(image)
        Firebase.auth.createUserWithEmailAndPassword(
            email.text.toString(),
            password.text.toString()
        ).addOnCompleteListener(this){

            if(it.isSuccessful){
                Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show()

                this.goHome(view)
            } else {
                Toast.makeText(this, "Fail to register, try again", Toast.LENGTH_SHORT).show()
            }
        }
        goHome(view)
    }

    fun registrarDatos(view : View?){

        var aux= "none"
        if(imageString!= null){
            aux= imageString
        }
        data class user(
            val name: String? = nombre.text.toString(),
            val email: String? = this.email.text.toString(),
            val username: String? = this.username.text.toString(),
            val photo : String? =aux,
            val goal: Int=0
        )
        var users= user()
        Firebase.firestore.collection("Users").document(this.email.text.toString())
            .set(users)
            .addOnSuccessListener {

            }
            .addOnFailureListener{
                Toast.makeText(this, "Fail EFE", Toast.LENGTH_SHORT).show()

            }
    }

    public fun clickedLogIn(v : View?) {

        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }


    public fun goHome(v : View?) {

        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email",email.text.toString())
        startActivity(intent)
        nombre.setText("")
        email.setText("")
        username.setText("")
        password.setText("")
    }
    public fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data!!
            imageView.setImageURI(imageUri)
            val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            imageString = BitMapToString(imageBitmap)
        }



    }

}