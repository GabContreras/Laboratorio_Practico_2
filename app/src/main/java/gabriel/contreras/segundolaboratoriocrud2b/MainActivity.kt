package gabriel.contreras.segundolaboratoriocrud2b

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import modelo.ClaseConexion

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtUsuario = findViewById<EditText>(R.id.txtUsuario)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasena)
        val btnIngresar = findViewById<Button>(R.id.btnAcceder)
        val btnRegistrarse = findViewById<TextView>(R.id.txtRegistrarse)


        btnIngresar.setOnClickListener {
            //Cambio de pantalla para agregar tickets
            val pantallaPrincipal = Intent(this,Ticket::class.java)

            val conexion = ClaseConexion().cadenaConexion()

            val comprobacionUsuario = conexion?.prepareStatement("SELECT * FROM TB_USUARIO WHERE NOMBRE_DE_USUARIO = ? AND CONTRASENA = ?")!!
            comprobacionUsuario.setString(1, txtUsuario.text.toString())
            comprobacionUsuario.setString(2, txtContrasena.text.toString())
            val resultado = comprobacionUsuario.executeQuery()

            if (resultado.next()) {
                startActivity(pantallaPrincipal)
            } else {
                println("Usuario inválido, compruebe credenciales")
            }

        }
        //Cambio de pantalla para poder registrarse
        btnRegistrarse.setOnClickListener {
            val pantalla2 = Intent(this, Registrarse::class.java)
            startActivity(pantalla2)
        }
    }

}