package apps.kata.spideymessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // Creando pantalla inicial de registro
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Accion al hacer click en el boton registro
        btnRegistro_registro.setOnClickListener {
            crearRegistro()

        }
        // Mostrando pantalla de Login
        lblTieneCuenta_registro.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun crearRegistro(){
        // Creacion de las variables para el inicio de registro
        val nombreUsuario = txtUsuario_registro.text.toString()
        val correo = txtCorreo_registro.text.toString()
        val password = txtPassword_registro.text.toString()

        // Validar que los valores de las variables nombreUsuario, correo y password no
        // se introduzcan con valores vacios
        if (nombreUsuario.isEmpty() || correo.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "No se pueden dejar datos en blanco", Toast.LENGTH_LONG).show()
            return
        }

        // Implementacion de Firebase
        // Creando una instancia de auteticacion con el correo y la contraseña
        // Obteniendo los valores de las variables correo y password
        // Creando registro de usuario
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, password)
            // Si el proceso de autenticacion no es completado con exito devolver al usurio para que lo complete
            .addOnCompleteListener {
                if (!it.isSuccessful){
                    return@addOnCompleteListener
                } else {
                    // si es completado con exito
                    Toast.makeText(this, "Su cuenta se ha creado con exito", Toast.LENGTH_SHORT).show()
                }
            }
            // Manipulando la Excepcion de ".addOnFailureListener", si falla al crear un usuario
            .addOnFailureListener{
                Toast.makeText(this, "Error al crear su cuenta, Porfavor ingrese los datos correctos", Toast.LENGTH_LONG).show()
            }
    }
}
