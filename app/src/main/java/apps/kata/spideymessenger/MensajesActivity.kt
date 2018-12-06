package apps.kata.spideymessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class MensajesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajes)
        /**
         * Verificando Acceso de Usuario
         */
        verificarUsuario()
    }

    private fun verificarUsuario(){
        val idUsuario = FirebaseAuth.getInstance().uid
        if (idUsuario == null){
            val intent = Intent (this, RegistroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_nuevoMensaje -> {
                val intent = Intent(this, NuevoMensajeActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_cerrarSesion -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegistroActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Agregando menu de opciones:
     * Nuevo Mensaje
     * Cerrar Sesion
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
