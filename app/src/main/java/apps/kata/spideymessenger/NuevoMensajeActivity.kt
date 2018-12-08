package apps.kata.spideymessenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast

class NuevoMensajeActivity : AppCompatActivity() {

    var lista: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_mensaje)

        val usuarios = ArrayList<Usuarios>()

        usuarios.add(Usuarios("Spidey Usuario", R.drawable.spideymessengerico))
        usuarios.add(Usuarios("Spidey Usuario", R.drawable.spideymessengerico))
        usuarios.add(Usuarios("Spidey Usuario", R.drawable.spideymessengerico))
        usuarios.add(Usuarios("Spidey Usuario", R.drawable.spideymessengerico))

        lista = findViewById(R.id.rv_NuevosMensajes)
        lista?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        lista?.layoutManager = layoutManager

        adaptador = AdaptadorCustom(this, usuarios, object: ClickListener {
            override fun onClick(vista: View, index: Int) {
                Toast.makeText(applicationContext, usuarios.get(index).nombre, Toast.LENGTH_SHORT).show()
            }
        }, object: LongClickListener {
            override fun LongClickListener(vista: View, index: Int) {
                Log.d("LONG", "Long listenersito")
            }
        })

        lista?.adapter = adaptador
    }
}

