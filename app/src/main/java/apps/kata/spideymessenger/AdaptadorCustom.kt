package apps.kata.spideymessenger

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class AdaptadorCustom(var context: Context, var items: ArrayList<Usuarios>, var listener: ClickListener, var longClickListener: LongClickListener): RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): AdaptadorCustom.ViewHolder {
        val vista = LayoutInflater.from(context).inflate(R.layout.activity_template, parent, false)

        return ViewHolder(vista, listener, longClickListener)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: AdaptadorCustom.ViewHolder, position: Int) {
        val item = items.get(position)
        holder.foto?.setImageResource(item.foto)
        holder.nombre?.text = item.nombre
    }

    class ViewHolder(var vista: View, var listener: ClickListener, var longClickListener: LongClickListener): RecyclerView.ViewHolder(vista), View.OnClickListener, View.OnLongClickListener {
        override fun onLongClick(v: View?): Boolean {
            longClickListener.LongClickListener(vista, adapterPosition)

            return true
        }

        override fun onClick(v: View?) {
            listener.onClick(vista, adapterPosition)
        }

        var foto: ImageView? = null
        var nombre: TextView? = null


        init {
            foto = vista.findViewById(R.id.imgUsuarioSpidey)
            nombre = vista.findViewById(R.id.tvSpideyUsario)

            vista.setOnClickListener(this)
            vista.setOnLongClickListener(this)
        }
    }

}