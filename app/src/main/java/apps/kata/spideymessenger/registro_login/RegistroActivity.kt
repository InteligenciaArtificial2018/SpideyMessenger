package apps.kata.spideymessenger.registro_login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import apps.kata.spideymessenger.R
import apps.kata.spideymessenger.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registro.*
import java.util.*

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        /**
         * Accion al hacer click en el boton registro
         */
        btnRegistro_registro.setOnClickListener {
            crearRegistro()
        }
        /**
         * Mostrando pantalla de Login
         */
        lblTieneCuenta_registro.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Boton agregar foto
        btnFoto_registro.setOnClickListener {
            /**
             * Accediendo a la galeria de fotos para seleccionar una imagen de perfil
             */
            val intent = Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, 0)
            //Toast.makeText(this,"Intentando mostrar foto para seleccionarla",Toast.LENGTH_SHORT).show()
        }
    }

    var ubicacionImagenSeleccionada: Uri? = null
    /**
     * Sobre escribiendo funcion de Activity Result para capturar la imagen del usuario
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            /**
             * Proceder a verificar que imagen fue seleccionada
             */
            //Toast.makeText(this,"La foto fue seleccionada",Toast.LENGTH_SHORT).show()
            /**
             * Encontrando la ubicacion de la imagen
             */
            ubicacionImagenSeleccionada = data.data

            // Obteniendo la imagen del usuario
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ubicacionImagenSeleccionada)

            /**
             * Agregando imagen seleccionada al boton de "Agregar Foto"
             */
            imgAgregarFoto_registro.setImageBitmap(bitmap)
            btnFoto_registro.alpha = 0f

            /**
             * Agregar imagen al boton de Agregar Foto
             */

            //val bitmapDrawable = BitmapDrawable(bitmap)
            //btnFoto_registro.setBackgroundDrawable(bitmapDrawable)

        }
    }

    /**
     * Funcion para el registro de Usuario
     */
    private fun crearRegistro(){
        /**
         * Creacion de las variables para el inicio de registro
         */
        val nombreUsuario = txtNombreUsuario_registro.text.toString()
        val correo = txtCorreo_registro.text.toString()
        val password = txtPassword_registro.text.toString()

        // Validar que los valores de las variables nombreUsuario, correo y password no
        // se introduzcan con valores vacios
        if (nombreUsuario.isEmpty() || correo.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "No se pueden dejar datos en blanco", Toast.LENGTH_LONG).show()
            return
        }
        /**
         *  Implementacion de Firebase
            Creando una instancia de auteticacion con el correo y la contraseña
            Obteniendo los valores de las variables correo y password
            Creando registro de usuario
         */
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, password)
            /**
             * Si el proceso de autenticacion no es completado con exito devolver al usuario para que lo complete
             */
            .addOnCompleteListener {
                if (!it.isSuccessful){
                    return@addOnCompleteListener
                } else {
                    /**
                     * si es completado con exito
                     */
                    Toast.makeText(this, "Su cuenta se ha creado con exito", Toast.LENGTH_SHORT).show()
                    /**
                     * Agregar imagen a Firebase
                     */
                    cargarImagenAFireBase()
                }
            }
            /**
             * Manipulando la Excepcion de ".addOnFailureListener", si falla al crear un usuario
             */
            .addOnFailureListener{
                Toast.makeText(this, "Error al crear su cuenta, Porfavor ingrese los datos correctos", Toast.LENGTH_LONG).show()
            }
    }

    private fun cargarImagenAFireBase(){

        if (ubicacionImagenSeleccionada == null ){
            return
        }

        /**
         * Agregado un nombre de archivo con ID Unico para cada usuario
         */
        val nombreArchivo = UUID.randomUUID().toString()

        /**
         * Agregando la imagen del usuario al almacenamiento creado en FireBase
         */
        val referenciaAlmacenamiento = FirebaseStorage.getInstance().getReference("/imagenesUsuario/$nombreArchivo")

        referenciaAlmacenamiento.putFile(ubicacionImagenSeleccionada!!)
            .addOnSuccessListener {

                /**
                 * Descarga de imagen de Storage Firebase
                 */
                referenciaAlmacenamiento.downloadUrl.addOnSuccessListener{
                    it.toString()
                    //Toast.makeText(this, "La localizacionn de la imagen es: $it", Toast.LENGTH_LONG).show()
                    guardarUsuarioAFireBase(it.toString())
                }
            }
            .addOnFailureListener{

            }
        }

    /**
     * Guardar todos los datos de usuario en la Base de Datos de Firebase
     */
    private fun guardarUsuarioAFireBase(imagenPerfil: String){
        val idUsuario = FirebaseAuth.getInstance().uid ?: ""
        val referenciaBaseDatos = FirebaseDatabase.getInstance().getReference("/infoUsuarios/$idUsuario")
        val usuario = Usuario(
            idUsuario,
            txtNombreUsuario_registro.text.toString(),
            imagenPerfil
        )
        referenciaBaseDatos.setValue(usuario)
            .addOnSuccessListener {
                //Toast.makeText(this, "Se ha guardado con exito el usuario en Storage", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, UltimosMensajesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}