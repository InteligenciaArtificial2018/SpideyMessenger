package apps.kata.spideymessenger.mensajeria

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import apps.kata.spideymessenger.R
import apps.kata.spideymessenger.modelos.MensajesChat
import apps.kata.spideymessenger.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_enviar_mensaje.view.*
import kotlinx.android.synthetic.main.chat_recibir_mensaje.view.*


class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        /**
         * Agregando a la barra de titulo el nombre del usuario
         */
        val usuario =  intent.getParcelableExtra<Usuario>(NuevoMensajeActivity.USER_kEY)
        supportActionBar?.title = usuario.nombreUsuario

        pruebaEnvioMensajes()

        btnEnviar.setOnClickListener {
            Log.d(TAG, "Intentando enviar mensajes...")

            enviarMensajes()
        }
    }


    /**
     * Esta funcion se encargara de enviar y guardar los mensajes en Firebase
     */
    private fun enviarMensajes (){
        // Como enviar mensajes con Firebase
        val texto = txtEscribirMensaje.text.toString()

        val recibirId = FirebaseAuth.getInstance().uid
        val usuario =  intent.getParcelableExtra<Usuario>(NuevoMensajeActivity.USER_kEY)
        val enviarId = usuario.idUsuario

        if ( recibirId == null){
            return
        }

        val referencia = FirebaseDatabase.getInstance().getReference("mensajesUsuarios").push()
        val mensajesChat = MensajesChat(referencia.key!!, texto, recibirId, enviarId, System.currentTimeMillis() / 1000 )
        referencia.setValue(mensajesChat)
            .addOnSuccessListener {
                Log.d(TAG, "Se guardo su mensaje: ${referencia.key}")
            }
    }

    private fun pruebaEnvioMensajes(){
        /**
         * Traer lista de mensajes
         */
        val adaptador = GroupAdapter<ViewHolder>()

        adaptador.add(ItemRecibirChat("Recibiendo mensajes........"))
        adaptador.add(ItemEnviarChat("Enviando mensajes\nMensajes."))
        adaptador.add(ItemRecibirChat("Recibiendo mensajes........"))
        adaptador.add(ItemEnviarChat("Enviando mensajes\nMensajes."))
        adaptador.add(ItemRecibirChat("Recibiendo mensajes........"))
        adaptador.add(ItemEnviarChat("Enviando mensajes\nMensajes."))
        adaptador.add(ItemRecibirChat("Recibiendo mensajes........"))
        adaptador.add(ItemEnviarChat("Enviando mensajes\nMensajes."))

        rvChatLog.adapter = adaptador
    }
}

/**
 * Cargar Items para enviar chat
 */
class ItemRecibirChat(val texto: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //viewHolder.itemView.text.tvRecibirMensaje.text = "Recibiendo mensaje..."
        viewHolder.itemView.tvRecibirMensaje.text = texto
    }
    override fun getLayout(): Int {
        return R.layout.chat_recibir_mensaje
    }
}

/**
 * Cargar Items para recibir chat
 */
class ItemEnviarChat(val texto: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //viewHolder.itemView.text.tvEnviarMensaje.text = "Enviando mensaje..."
        viewHolder.itemView.tvEnviarMensaje.text = texto
    }
    override fun getLayout(): Int {
        return R.layout.chat_enviar_mensaje
    }
}
