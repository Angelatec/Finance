package mx.tec.finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.activity.result.contract.ActivityResultContracts
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.google.firebase.auth.ktx.auth
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


class LogInActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100
    private val callbackManager = CallbackManager.Factory.create()


    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var mail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
    }



    fun login(view : View?){

        Firebase.auth.signInWithEmailAndPassword(
            email.text.toString(),
            password.text.toString()
        ).addOnCompleteListener(this){

            if(it.isSuccessful) {

                Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show()
                this.goHome(view)
            }
            else
                Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show()
        }
    }

    public fun clickedRegister(v : View?) {

        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    public fun goHome(v : View?) {

        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email",email.text.toString())
        startActivity(intent)
    }
    public fun resetPassword(v: View?){
        val intent = Intent(this, ChangePassword::class.java)
        startActivity(intent)
    }



    public fun googleLogin(v: View?){
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)
        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
    }

    public fun facebookLogin(v:View?){

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))


        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult>{

                override fun onSuccess(result: LoginResult?) {
                    result?.let{
                        val token = it.accessToken

                        val credential = FacebookAuthProvider.getCredential(token.token)
                        //showHome()
                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                            if (it.isSuccessful) {

                                Log.d("Facebook", Firebase.auth.currentUser?.email.toString())

                                data class user(
                                    val name: String? = Firebase.auth.currentUser?.displayName.toString(),
                                    val email: String? = Firebase.auth.currentUser?.email.toString(),
                                    val username: String? = Firebase.auth.currentUser?.displayName.toString(),
                                    val photo : String? = "no",
                                    val goal: Int=0


                                )
                                var users= user()

                                Firebase.firestore.collection("Users").document(Firebase.auth.currentUser?.email.toString())
                                    .set(users)
                                    .addOnSuccessListener {

                                    }
                                    .addOnFailureListener{
                                        showError()

                                    }

                                showHome(Firebase.auth.currentUser?.email.toString(), "no")
                            }else{
                                showError()
                            }
                        }
                    }
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {
                    showError()
                }

            })
    }

    public fun showHome(my_email:String, img:String){
        val intent = Intent(this, HomeActivity::class.java)

        intent.putExtra("email",my_email)
        intent.putExtra("image",img)

        startActivity(intent)
    }
    public fun showError(){
        Toast.makeText(this, "Fail EFE", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            data class user(
                                val name: String? = Firebase.auth.currentUser?.displayName.toString(),
                                val email: String? = Firebase.auth.currentUser?.email.toString(),
                                val username: String? = Firebase.auth.currentUser?.displayName.toString(),
                                val photo : String? = "no",
                                val goal: Int=0


                            )
                            var users= user()

                            Firebase.firestore.collection("Users").document(Firebase.auth.currentUser?.email.toString())
                                .set(users)
                                .addOnSuccessListener {

                                }
                                .addOnFailureListener{
                                    showError()

                                }
                            showHome(Firebase.auth.currentUser?.email.toString(), "no")
                        }
                    }
                }
            }catch (e: ApiException){

            }

        }
    }
}