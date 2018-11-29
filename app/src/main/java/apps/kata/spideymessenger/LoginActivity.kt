package apps.kata.spideymessenger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    //Creando pantalla de Login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        lblVolverRegistro_Login.setOnClickListener {
            // Mostrando Pantalla de Registro
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}