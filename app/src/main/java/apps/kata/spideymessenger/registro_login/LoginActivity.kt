package apps.kata.spideymessenger.registro_login


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import apps.kata.spideymessenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Accion al hacer click en el boton Iniciar Sesión
        btnIniciar_login.setOnClickListener {
            val correo = txtCorreo_login.text.toString()
            val password = txtPassword_login.text.toString()

            // Iniciando Login de usuario ya registrado
            FirebaseAuth.getInstance().signInWithEmailAndPassword(correo, password)
        }

        // Volver a pantalla de registro
        lblVolverRegistro_login.setOnClickListener {
            finish()
        }
    }
}