package gabriel.contreras.segundolaboratoriocrud2b

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class detalle_ticket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_ticket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Recibir los valores
        val UUIDRecibido = intent.getStringExtra("NUMERO_DE_TICKET")
        val tituloRecibido = intent.getStringExtra("TITULO")
        val descripcionRecibida = intent.getStringExtra("DESCRIPCION")
        val autorRecibido = intent.getStringExtra("AUTOR")
        val emailRecibido = intent.getStringExtra("EMAIL_AUTOR")
        val fechaCreacionRecibida = intent.getStringExtra("FECHA_DE_CREACION")
        val estadoRecibido = intent.getStringExtra("ESTADO")



        //Mando a llamar a todos los elementos de la pantalla
        val txtUUIDDetalle = findViewById<TextView>(R.id.txtUUIDDetalle)
        val txtTituloDetalle = findViewById<TextView>(R.id.txtTituloDetalle)
        val txtDescripcionDetalle = findViewById<TextView>(R.id.txtDescripcionDetalle)
        val txtAutorDetalle = findViewById<TextView>(R.id.txtAutorDetalle)
        val txtEmailDetalle = findViewById<TextView>(R.id.txtEmailAutorDetalle)
        val txtFechaCreacionDetalle = findViewById<TextView>(R.id.txtFechadeCreacionDetalle)
        val txtEstadoDetalle = findViewById<TextView>(R.id.txtEstadoDetalle
        )


        //Asigna datos recibidos a los TextView
        txtUUIDDetalle.text = UUIDRecibido
        txtTituloDetalle.text = tituloRecibido
        txtDescripcionDetalle.text = descripcionRecibida
        txtAutorDetalle.text = autorRecibido
        txtEmailDetalle.text = emailRecibido
        txtFechaCreacionDetalle.text = fechaCreacionRecibida
        txtEstadoDetalle.text = estadoRecibido
    }
}