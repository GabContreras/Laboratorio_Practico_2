package gabriel.contreras.segundolaboratoriocrud2b

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.util.UUID

class Registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val txtUsuarioRegistrarse = findViewById<EditText>(R.id.txtUsuarioRegistro)
        val txtContrasenaRegistrarse = findViewById<EditText>(R.id.txtContrasenaRegistro)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)



        btnRegistrarse.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {

                val objConexion = ClaseConexion().cadenaConexion()


                val crearUsuario =
                    objConexion?.prepareStatement("INSERT INTO TB_USUARIO(UUID_USUARIO, NOMBRE_DE_USUARIO, CONTRASENA) VALUES (?, ?, ?)")!!
                crearUsuario.setString(1, UUID.randomUUID().toString())
                crearUsuario.setString(2, txtUsuarioRegistrarse.text.toString())
                crearUsuario.setString(3, txtContrasenaRegistrarse.text.toString())
                crearUsuario.executeUpdate()
                withContext(Dispatchers.Main) {

                    Toast.makeText(this@Registrarse, "Usuario creado, Regresa a la pantalla anterior para poder iniciar sesión en el sistema", Toast.LENGTH_LONG).show()
                    txtUsuarioRegistrarse.setText("")
                    txtContrasenaRegistrarse.setText("")
                }
            }

        }

    }
}