package apps.kata.spideymessenger.vistas

import apps.kata.spideymessenger.R
import apps.kata.spideymessenger.modelos.MensajesChat
import apps.kata.spideymessenger.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.lista_mensajes.view.*


class UltimosMensajesLista(val mensajesChat: MensajesChat): Item<ViewHolder>() {
  var chatAmigo: Usuario? = null

  override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.tv_lista_mensaje.text = mensajesChat.texto

    val chatPartnerId: String
    if (mensajesChat.deId == FirebaseAuth.getInstance().uid) {
      chatPartnerId = mensajesChat.paraId
    } else {
      chatPartnerId = mensajesChat.deId
    }

    val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
    ref.addListenerForSingleValueEvent(object: ValueEventListener {
      override fun onDataChange(p0: DataSnapshot) {
        chatAmigo = p0.getValue(Usuario::class.java)
        viewHolder.itemView.tv_usuario_lista_mensaje.text = chatAmigo?.nombreUsuario

        val targetImageView = viewHolder.itemView.img_lista_mensaje
        Picasso.get().load(chatAmigo?.imagenPerfil).into(targetImageView)
      }

      override fun onCancelled(p0: DatabaseError) {
      }
    })
  }

  override fun getLayout(): Int {
    return R.layout.lista_mensajes
  }
}