package pe.com.grupo3.eventosulima.models

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pe.com.grupo3.eventosulima.Constantes
import pe.com.grupo3.eventosulima.models.beans.Usuario

class GestorUsuarios {
    val dbFirebase = Firebase.firestore

    companion object {
        private var instance : GestorUsuarios? = null

        fun getInstance() : GestorUsuarios {
            if(instance == null) {
                instance = GestorUsuarios()
            }
            return instance!!
        }
    }

    fun login(username : String, password : String, callback : (Usuario?) -> Unit) {
        val usuariosCol = dbFirebase.collection("Usuarios")

        usuariosCol.whereEqualTo("username", username)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener {
                if(it!!.documents.size > 0){
                    val usuario = Usuario(
                        it.documents[0]["username"].toString(),
                        it.documents[0]["nombres"].toString(),
                        it.documents[0]["password"].toString(),
                        it.documents[0]["apellidos"].toString(),
                        Integer.parseInt(it.documents[0]["edad"].toString()),
                        Integer.parseInt(it.documents[0]["codigoULima"].toString())
                    )
                    callback(usuario)
                }else {
                    callback(null)
                }
            }
    }

    fun guardarUsuarioFirebase(usuario : Usuario, success : () -> Unit, error: (String) -> Unit) {
        val usuariosCol = dbFirebase.collection("Usuarios")


        dbFirebase.runTransaction{
            it.set(usuariosCol.document(), usuario)
        }.addOnSuccessListener {
            success()
        }.addOnFailureListener {
            error(it.message.toString())
        }
    }

/*    fun obtenerNombre(username : String, callback: (List<String>?) -> Unit *//*success : () -> Unit, error: (String) -> Unit*//*) {
        dbFirebase.collection("Usuarios")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    val usuario = listOf(
                        document.data["username"] as String,
                        document.data["nombres"] as String,
                        document.data["password"] as String,
                        document.data["apellidos"] as String,
                        (document.data["edad"] as Int).toString(),
                        (document.data["codigoUlima"] as Int).toString())
                    callback(usuario)
                }
            }
            .addOnFailureListener {
                error(it.message.toString())
            }
    }*/
}