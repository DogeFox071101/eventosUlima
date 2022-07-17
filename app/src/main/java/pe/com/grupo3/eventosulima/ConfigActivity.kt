package pe.com.grupo3.eventosulima

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import pe.com.grupo3.eventosulima.models.GestorUsuarios

class ConfigActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar
    private lateinit var imgViewUsuario : ImageView
    private lateinit var txtViewUsuario : TextView
    private lateinit var txtViewLogout : TextView
    private lateinit var constLayEditInfo : ConstraintLayout
    private lateinit var constLayEditPass : ConstraintLayout
    private lateinit var constLayEditNotif : ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        toolbar = findViewById(R.id.toolbar)
        imgViewUsuario = findViewById(R.id.imgViewUsuario)
        txtViewUsuario = findViewById(R.id.txtViewUsuario)
        txtViewLogout = findViewById(R.id.txtViewLogout)
        constLayEditInfo = findViewById(R.id.constLayEditInfo)
        constLayEditPass = findViewById(R.id.constLayEditPass)
        constLayEditNotif = findViewById(R.id.constLayEditNotif)

        setSupportActionBar(toolbar)

        toolbar.title = ""
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val getSP = getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE)
        val nombreUsuario = getSP.getString(Constantes.NOMBRES, "Usuario")
        val apellidoUsuario = getSP.getString(Constantes.APELLIDOS, "")

        txtViewUsuario.text = nombreUsuario!! + " " + apellidoUsuario!!
        txtViewLogout.setOnClickListener {
            cerrarSesion()
        }
        constLayEditInfo.setOnClickListener {
            val intent = Intent(this, ConfigInfoActivity::class.java)
            startActivity(intent)
        }
        constLayEditPass.setOnClickListener {
            val intent = Intent(this, ConfigPassActivity::class.java)
            startActivity(intent)
        }
        constLayEditNotif.setOnClickListener {
            val intent = Intent(this, ConfigNotifActivity::class.java)
            startActivity(intent)
        }
        imgViewUsuario.setOnClickListener {
            val intent = Intent(this, ConfigFotoActivity::class.java)
            startActivity(intent)
        }
    }
    private fun cerrarSesion() {
        val editor = getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE).edit()
        editor.putBoolean(Constantes.SP_ESTA_LOGEADO, false)
        editor.putString(Constantes.USERNAME, "")
        editor.putString(Constantes.APELLIDOS, "")
        editor.putString(Constantes.NOMBRES, "")
        editor.putString(Constantes.CODIGO_ULIMA, "")
        editor.putString(Constantes.EDAD, "")
        editor.commit()

        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}