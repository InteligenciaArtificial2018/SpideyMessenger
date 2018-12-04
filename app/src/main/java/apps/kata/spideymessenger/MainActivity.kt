package apps.kata.spideymessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    // Creando pantalla inicial de registro
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Accion al hacer click en el boton registro
        btnRegistro_registro.setOnClickListener {
            crearRegistro()
        }
        // Mostrando pantalla de Login
        lblTieneCuenta_registro.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Boton agregar foto
        btnFoto_registro.setOnClickListener {
            // Accediendo a la galeria de fotos para seleccionar una imagen de perfil
            val intent = Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, 0)
            Toast.makeText(this,"Intentando mostrar foto para seleccionarla",Toast.LENGTH_SHORT).show()
        }
    }

    var ubicacionImagenSeleccionada: Uri? = null
    // Sobre escribiendo funcion de Activity Result para capturar la imagen del usuario
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            // Proceder a verificar que imagen fue seleccionada
            Toast.makeText(this,"La foto fue seleccionada",Toast.LENGTH_SHORT).show()
            //Encontrando la ubicacion de la imagen
            ubicacionImagenSeleccionada = data.data

            // Obteniendo la imagen del usuario
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ubicacionImagenSeleccionada)

            //Trasformando la imagen mediante bitmap para hacerla una imagen circular
            val bitmapDrawable = BitmapDrawable(bitmap)
            btnFoto_registro.setBackgroundDrawable(bitmapDrawable)

        }
    }

    // Funcion para el registro de Usuario
    private fun crearRegistro(){
        // Creacion de las variables para el inicio de registro
        val nombreUsuario = txtUsuario_registro.text.toString()
        val correo = txtCorreo_registro.text.toString()
        val password = txtPassword_registro.text.toString()

        // Validar que los valores de las variables nombreUsuario, correo y password no
        // se introduzcan con valores vacios
        if (nombreUsuario.isEmpty() || correo.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "No se pueden dejar datos en blanco", Toast.LENGTH_LONG).show()
            return
        }

        // Implementacion de Firebase
        // Creando una instancia de auteticacion con el correo y la contraseña
        // Obteniendo los valores de las variables correo y password
        // Creando registro de usuario
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, password)
            // Si el proceso de autenticacion no es completado con exito devolver al usurio para que lo complete
            .addOnCompleteListener {
                if (!it.isSuccessful){
                    return@addOnCompleteListener
                } else {
                    // si es completado con exito
                    Toast.makeText(this, "Su cuenta se ha creado con exito", Toast.LENGTH_SHORT).show()
                    // Agregar imagen a Firebase
                    cargarImagenAFireBase()
                }
            }
            // Manipulando la Excepcion de ".addOnFailureListener", si falla al crear un usuario
            .addOnFailureListener{
                Toast.makeText(this, "Error al crear su cuenta, Porfavor ingrese los datos correctos", Toast.LENGTH_LONG).show()
            }
    }

    private fun cargarImagenAFireBase(){

        if (ubicacionImagenSeleccionada == null ){
            return
        }

        // Agregado un nombre de archivo con ID Unico para cada usuario
        val nombreArchivo = UUID.randomUUID().toString()
        // Agregando la imagen del usuario al almacenamiento creado en FireBase
        val referenciaAlmacenamiento = FirebaseStorage.getInstance().getReference("/imagenesUsuario/$nombreArchivo")

        referenciaAlmacenamiento.putFile(ubicacionImagenSeleccionada!!)
            .addOnSuccessListener {

                // Descarga de imagen de Storage Firebase
                referenciaAlmacenamiento.downloadUrl.addOnSuccessListener{
                    it.toString()
                    Toast.makeText(this, "La localizacionn de la imagen es: $it", Toast.LENGTH_LONG).show()
                    guardarUsuarioAFireBase()
                }
            }
        }
    // Guardar todos los datos de usuario en la Base de Datos de Firebase
    private fun guardarUsuarioAFireBase(){
        val idUsuario = FirebaseAuth.getInstance().uid
        val referenciaBaseDatos = FirebaseDatabase.getInstance().getReference("usuario/$idUsuario")
        //val usuario = UserInfo(idUsuario, txtUsuario_registro.text.toString())
        //referenciaBaseDatos.setValue()
    }
    class Usuario(val idUsuario:String, val nombreUsuario: String, val imagenPerfil: String)

}

