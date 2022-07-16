package pe.com.grupo3.eventosulima

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pe.com.grupo3.eventosulima.R
import pe.com.grupo3.eventosulima.models.GestorUsuarios

class LoginActivity: AppCompatActivity() {
    private lateinit var  btnRegistrarse: Button
    private lateinit var  btnIngresar : Button
    private lateinit var eteUsernameLogin : EditText
    private lateinit var etePasswordLogin : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)
        btnIngresar = findViewById(R.id.butIngresarLogin)
        eteUsernameLogin = findViewById(R.id.eteUsernameLogin)
        etePasswordLogin = findViewById(R.id.etePasswordLogin)
        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnIngresar.setOnClickListener {
            GestorUsuarios.getInstance().login(
                eteUsernameLogin.text.toString(),
                etePasswordLogin.text.toString()
            ) {
                if(it == null){
                    // Error en login
                    Toast.makeText(this, "Error Login",
                    Toast.LENGTH_SHORT).show()
                }else{
                   startActivity(Intent(this, MainActivity::class.java))
                   finish()
                }
            }
        }
    }
}