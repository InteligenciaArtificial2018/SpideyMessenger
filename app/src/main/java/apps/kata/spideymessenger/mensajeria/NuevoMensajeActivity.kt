package apps.kata.spideymessenger.mensajeria


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration

import android.util.Log
import apps.kata.spideymessenger.R
import apps.kata.spideymessenger.modelos.Usuario

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_nuevo_mensaje.*
import kotlinx.android.synthetic.main.activity_template.view.*

class NuevoMensajeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_mensaje)
        supportActionBar?.title = "Seleccionar Usuario"
        buscarUsuarios()
    }

    companion object {
        val USER_kEY = "USER_KEY"
    }
    private fun buscarUsuarios() {
        val ref = FirebaseDatabase.getInstance().getReference("/infoUsuarios")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adaptador = GroupAdapter<ViewHolder>()

                p0.children.forEach{
                    Log.d("NewMessage", it.toString())
                    val nombreUsuario = it.getValue(Usuario::class.java)
                    if (nombreUsuario != null){
                        adaptador.add(ItemsUsuarios(nombreUsuario))
                    }
                }

                adaptador.setOnItemClickListener { item, view ->
                    /**
                     * Enviar nombre de usuario mediante una llave al activity de ChatLog
                     */
                    val itemsUsuarios = item as ItemsUsuarios
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    //intent.putExtra(USER_kEY, itemsUsuarios.usuario.nombreUsuario)
                    intent.putExtra(USER_kEY, itemsUsuarios.usuario)
                    startActivity(intent)
                    finish()
                }
                
                rvNuevosMensajes.adapter = adaptador
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}

/**
 * Cargar Recycler View con los usuarios
 */
class ItemsUsuarios(val usuario: Usuario): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvNombreUsuario_template.text = usuario.nombreUsuario
        Picasso.get().load(usuario.imagenPerfil).into(viewHolder.itemView.imgUsuarioSpidey)
    }

    override fun getLayout(): Int {
        return R.layout.activity_template
    }
}
