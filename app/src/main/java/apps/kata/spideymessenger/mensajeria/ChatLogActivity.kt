package apps.kata.spideymessenger.mensajeria

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import apps.kata.spideymessenger.R
import apps.kata.spideymessenger.modelos.Usuario
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        /**
         * Agregando a la barra de titulo el nombre del usuario
         */
        val usuario =  intent.getParcelableExtra<Usuario>(NuevoMensajeActivity.USER_kEY)
        supportActionBar?.title = usuario.nombreUsuario

        /**
         * Traer lista de mensajes
         */
        val adaptador = GroupAdapter<ViewHolder>()

        adaptador.add(ItemEnviarChat())
        adaptador.add(ItemRecibirChat())
        adaptador.add(ItemEnviarChat())
        adaptador.add(ItemEnviarChat())

        rvChatLog.adapter = adaptador
    }
}

/**
 * Clase para llenar y adaptar contenido
 */

class ItemEnviarChat: Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
        return R.layout.activity_template_chat_enviar
    }
}

class ItemRecibirChat: Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
        return R.layout.activity_template_chat_recibir
    }
}
