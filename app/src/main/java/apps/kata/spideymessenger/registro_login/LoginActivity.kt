package apps.kata.spideymessenger.registro_login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import apps.kata.spideymessenger.R
import apps.kata.spideymessenger.mensajeria.UltimosMensajesActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_login)

    btnIniciar_login.setOnClickListener {
      realizarLogin()
    }

    lblVolverRegistro_login.setOnClickListener{
      finish()
    }
  }

  private fun realizarLogin() {
    val correo = txtCorreo_login.text.toString()
    val password = txtPassword_login.text.toString()

    if (correo.isEmpty() || password.isEmpty()) {
      Toast.makeText(this, "Porfavor ingrese correo y contraseña.", Toast.LENGTH_SHORT).show()
      return
    }

    FirebaseAuth.getInstance().signInWithEmailAndPassword(correo, password)
        .addOnCompleteListener {
          if (!it.isSuccessful) return@addOnCompleteListener

          val intent = Intent(this, UltimosMensajesActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
          startActivity(intent)
        }
        .addOnFailureListener {
          Toast.makeText(this, "Error al iniciar Login", Toast.LENGTH_SHORT).show()
        }
  }

}