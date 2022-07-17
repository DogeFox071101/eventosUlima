package pe.com.grupo3.eventosulima.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pe.com.grupo3.eventosulima.R
import pe.com.grupo3.eventosulima.models.beans.Pelicula

class ListadoPeliculasMainAdapter(private val mListaPeliculasMain : List<Pelicula>)
    : RecyclerView.Adapter<ListadoPeliculasMainAdapter.ViewHolder>(){
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val tviPeliculaNombre : TextView
        val tviPelicula : ImageView
        val tviDiaFuncion : TextView
        val tviHorario : TextView
        val tviRating : TextView
        val butCalificar : Button

        init {
            tviPeliculaNombre = view.findViewById(R.id.tviPeliculaNombre)
            tviPelicula = view.findViewById(R.id.ivPelicula)
            tviDiaFuncion = view.findViewById(R.id.tviDia)
            tviHorario = view.findViewById(R.id.tviHorario)
            tviRating = view.findViewById(R.id.tviRating)
            butCalificar = view.findViewById(R.id.butCalificar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pelicula, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelicula = mListaPeliculasMain[position]
        holder.tviPeliculaNombre.text = pelicula.titulo
        holder.tviDiaFuncion.text = pelicula.diaFuncion
        holder.tviHorario.text = pelicula.horaInicio
        holder.tviRating.text = "" + pelicula.rating

        Picasso.get()
            .load(pelicula.urlImagen)
            .resize(100, 150)
            .centerCrop()
            .error(R.mipmap.ic_launcher_round)
            .into(holder.tviPelicula)

    }

    override fun getItemCount(): Int {
        return mListaPeliculasMain.size
    }
}