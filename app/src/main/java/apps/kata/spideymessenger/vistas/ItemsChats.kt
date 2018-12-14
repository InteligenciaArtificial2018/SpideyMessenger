package apps.kata.spideymessenger.vistas

import apps.kata.spideymessenger.R
import apps.kata.spideymessenger.modelos.Usuario
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_mensaje_de.view.*
import kotlinx.android.synthetic.main.chat_mensaje_para.view.*

/**
 * Cargando los datos del usuario para interactuar con mensajes
 */
class ChatDeItem (val texto: String, val usuario: Usuario): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_de_lista.text = texto
        // Reemplaza la imagen de Spidey Logo para cargar la imagen de perfil del usuario
        val imagenPerfil = usuario.imagenPerfil
        val imagenPerfilUsuario = viewHolder.itemView.img_chat_de_lista
        // Captura la imagen paraser tomada por la libreria Picasso y transformarla
        Picasso.get().load(imagenPerfil).into(imagenPerfilUsuario)
    }

    override fun getLayout(): Int {
        return R.layout.chat_mensaje_de
    }
}

class ChatParaItem (val texto: String, val usuario: Usuario): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_para_lista.text = texto
        // Reemplaza la imagen de Spidey Logo para cargar la imagen de perfil del usuario
        val imagenPerfil = usuario.imagenPerfil
        val imagenPerfilUsuario = viewHolder.itemView.img_chat_para_lista
        // Captura la imagen paraser tomada por la libreria Picasso y transformarla
        Picasso.get().load(imagenPerfil).into(imagenPerfilUsuario)
    }

    override fun getLayout(): Int {
        return R.layout.chat_mensaje_para
    }
}