package apps.kata.spideymessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // Creando pantalla inicial de registro
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Accion al hacer click en el boton registro
        btnRegistro_login.setOnClickListener {
            // Creacion de las variables para el inicio de registro
            val nombreUsuario = txtNombreUsuario_registro.text.toString()
            val correo = txtCorreo_registro.text.toString()
            val password = txtPassword_registro.text.toString()
            //Toast.makeText(this, "Registrado con exito", Toast.LENGTH_SHORT).show()
            // Implementacion de Firebase

        }

        // Accion al hacer click en la etiqueta "Ya tengo una cuenta"
        lblTieneCuenta_registro.setOnClickListener {
            // Mostrando pantalla de Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
