package pe.com.grupo3.eventosulima

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class ConfigFotoActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_foto)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.foto_title)
    }
}