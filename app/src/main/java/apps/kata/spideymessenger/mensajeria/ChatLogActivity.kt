package apps.kata.spideymessenger.mensajeria

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import apps.kata.spideymessenger.R
import apps.kata.spideymessenger.modelos.MensajesChat
import apps.kata.spideymessenger.modelos.Usuario
import apps.kata.spideymessenger.vistas.ChatDeItem
import apps.kata.spideymessenger.vistas.ChatParaItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

  companion object {
    val TAG = "ChatLog"
  }

  val adapter = GroupAdapter<ViewHolder>()

  var paraUsuario: Usuario? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat_log)

    rvChatLog.adapter = adapter

    paraUsuario = intent.getParcelableExtra<Usuario>(NuevoMensajeActivity.USER_kEY)

    supportActionBar?.title = paraUsuario?.nombreUsuario

//    setupDummyData()
    listenForMessages()

    btnEnviar.setOnClickListener {
      Log.d(TAG, "Attempt to send message....")
      performSendMessage()
    }
  }

  private fun listenForMessages() {
    val fromId = FirebaseAuth.getInstance().uid
    val toId = paraUsuario?.idUsuario
    val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

    ref.addChildEventListener(object: ChildEventListener {

      override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val chatMessage = p0.getValue(MensajesChat::class.java)

        if (chatMessage != null) {
          Log.d(TAG, chatMessage.texto)

          if (chatMessage.deId == FirebaseAuth.getInstance().uid) {
            val currentUser = UltimosMensajesActivity.usuarioActual ?: return
            adapter.add(ChatDeItem(chatMessage.texto, currentUser))
          } else {
            adapter.add(ChatParaItem(chatMessage.texto, paraUsuario!!))
          }
        }

        rvChatLog.scrollToPosition(adapter.itemCount - 1)

      }

      override fun onCancelled(p0: DatabaseError) {}
      override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
      override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
      override fun onChildRemoved(p0: DataSnapshot) {
      }

    })

  }

  private fun performSendMessage() {
    // how do we actually send a message to firebase...
    val text = txtEscribirMensaje.text.toString()

    val deId = FirebaseAuth.getInstance().uid
    val usuario = intent.getParcelableExtra<Usuario>(NuevoMensajeActivity.USER_kEY)
    val paraId = usuario.idUsuario

    if (deId == null) return

//    val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
    val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$deId/$paraId").push()

    val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$paraId/$deId").push()

    val chatMessage = MensajesChat(reference.key!!, text, deId, paraId, System.currentTimeMillis() / 1000)

    reference.setValue(chatMessage)
        .addOnSuccessListener {
          Log.d(TAG, "Saved our chat message: ${reference.key}")
          txtEscribirMensaje.text.clear()
          rvChatLog.scrollToPosition(adapter.itemCount - 1)
        }

    toReference.setValue(chatMessage)

    val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$deId/$paraId")
    latestMessageRef.setValue(chatMessage)

    val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$paraId/$deId")
    latestMessageToRef.setValue(chatMessage)
  }
}
