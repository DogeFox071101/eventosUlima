package pe.com.grupo3.eventosulima.models

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.os.Handler
import pe.com.grupo3.eventosulima.models.beans.Evento
import pe.com.grupo3.eventosulima.models.beans.Pelicula

class GestorEventos {
    val dbFirebase = Firebase.firestore

    val handler : Handler = Handler()

    fun obtenerListaEventosFirebase(success : (List<Evento>) -> Unit, error : (String) -> Unit) {
        val eventosCol = dbFirebase.collection("Eventos")

        eventosCol.get()
            .addOnSuccessListener {
                val listaEventos = it.documents.map { documentSnapshot ->
                    Evento(
                        documentSnapshot["titulo"].toString(),
                        documentSnapshot["urlImagen"].toString(),
                        documentSnapshot["director"].toString(),
                        documentSnapshot["actores"].toString(),
                        documentSnapshot["duracion"].toString(),
                        documentSnapshot["genero"].toString(),
                        documentSnapshot["diaFuncion"].toString(),
                        documentSnapshot["horaInicio"].toString(),
                        documentSnapshot["tipoEvento"].toString(),
                        documentSnapshot["rating"].toString().toDouble(),
                        Integer.parseInt(documentSnapshot["capacidad"].toString())
                    )
                }
                success(listaEventos)
            }.addOnFailureListener {
                error(it.message.toString())
            }
    }

    fun guardarListaEventosFirebase(eventos : List<Evento>, success : () -> Unit, error: (String) -> Unit) {
        val eventosCol = dbFirebase.collection("Eventos")

        dbFirebase.runTransaction { transaction ->
            eventos.forEach {
                val mapEvento = hashMapOf(
                    "titulo" to it.titulo,
                    "urlImagen" to it.urlImagen,
                    "director" to it.director,
                    "actores" to it.actores,
                    "duracion" to it.duracion,
                    "genero" to it.genero,
                    "diaFuncion" to it.diaFuncion,
                    "horaInicio" to it.horaInicio,
                    "tipoEvento" to it.tipoEvento,
                    "rating" to it.rating,
                    "capacidad" to it.capacidad
                )
                transaction.set(eventosCol.document(), mapEvento)
            }
        }.addOnSuccessListener {
            success()
        }.addOnFailureListener {
            error(it.message.toString())
        }
    }
}