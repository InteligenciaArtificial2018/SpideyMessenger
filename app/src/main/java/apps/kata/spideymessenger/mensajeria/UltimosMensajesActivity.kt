package apps.kata.spideymessenger.mensajeria

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import apps.kata.spideymessenger.R
import apps.kata.spideymessenger.modelos.MensajesChat
import apps.kata.spideymessenger.modelos.Usuario
import apps.kata.spideymessenger.registro_login.RegistroActivity
import apps.kata.spideymessenger.vistas.UltimosMensajesLista
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_ultimos_mensajes.*


class UltimosMensajesActivity : AppCompatActivity() {

  companion object {
    var usuarioActual: Usuario? = null
    val TAG = "UltimosMensajes"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_ultimos_mensajes)

    rv_ultimos_mensajes.adapter = adapter
    rv_ultimos_mensajes.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    // set item click listener on your adapter
    adapter.setOnItemClickListener { item, view ->
      Log.d(TAG, "123")
      val intent = Intent(this, ChatLogActivity::class.java)

      // we are missing the chat partner user

      val lista = item as UltimosMensajesLista
      intent.putExtra(NuevoMensajeActivity.USER_kEY, lista.chatAmigo)
      startActivity(intent)
    }

//    setupDummyRows()
    listenForLatestMessages()

    fetchCurrentUser()

    verifyUserIsLoggedIn()
  }

  val latestMessagesMap = HashMap<String, MensajesChat>()

  private fun refreshRecyclerViewMessages() {
    adapter.clear()
    latestMessagesMap.values.forEach {
      adapter.add(UltimosMensajesLista(it))
    }
  }

  private fun listenForLatestMessages() {
    val fromId = FirebaseAuth.getInstance().uid
    val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
    ref.addChildEventListener(object: ChildEventListener {
      override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val chatMessage = p0.getValue(MensajesChat::class.java) ?: return
        latestMessagesMap[p0.key!!] = chatMessage
        refreshRecyclerViewMessages()
      }

      override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        val chatMessage = p0.getValue(MensajesChat::class.java) ?: return
        latestMessagesMap[p0.key!!] = chatMessage
        refreshRecyclerViewMessages()
      }

      override fun onChildMoved(p0: DataSnapshot, p1: String?) {

      }
      override fun onChildRemoved(p0: DataSnapshot) {

      }
      override fun onCancelled(p0: DatabaseError) {

      }
    })
  }

  val adapter = GroupAdapter<ViewHolder>()

//  private fun setupDummyRows() {
//
//
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//  }

  private fun fetchCurrentUser() {
    val uid = FirebaseAuth.getInstance().uid
    val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
    ref.addListenerForSingleValueEvent(object: ValueEventListener {

      override fun onDataChange(p0: DataSnapshot) {
        usuarioActual = p0.getValue(Usuario::class.java)
        Log.d("LatestMessages", "Current user ${usuarioActual?.imagenPerfil}")
      }

      override fun onCancelled(p0: DatabaseError) {

      }
    })
  }

  private fun verifyUserIsLoggedIn() {
    val uid = FirebaseAuth.getInstance().uid
    if (uid == null) {
      val intent = Intent(this, RegistroActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
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
        val intent = Intent(this, NuevoMensajeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
      }
    }

    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.nav_menu, menu)
    return super.onCreateOptionsMenu(menu)
  }

}
