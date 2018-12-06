package apps.kata.spideymessenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class NuevoMensajeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_mensaje)

        supportActionBar?.title = "Seleccionar Usuario"
    }
}
