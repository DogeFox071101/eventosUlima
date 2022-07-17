package pe.com.grupo3.eventosulima

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class ConfigInfoActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_info)

        toolbar = findViewById(R.id.toolbar)

        toolbar.title = getString(R.string.edit_title)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}