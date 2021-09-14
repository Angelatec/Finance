package mx.tec.finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainPageActivity : AppCompatActivity() {

    lateinit var buttonLog : Button
    lateinit var buttonReg : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

    }
    public fun clickedLogIn(v : View?) {

        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }

    public fun clickedRegister(v : View?) {

        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


}