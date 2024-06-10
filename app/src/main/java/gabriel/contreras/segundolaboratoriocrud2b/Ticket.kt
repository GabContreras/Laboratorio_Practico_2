package gabriel.contreras.segundolaboratoriocrud2b

import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaTickets
import java.util.UUID

class Ticket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ticket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //1- Mandar a llamar todos los elementos de la vista
        val txtTitulo = findViewById<EditText>(R.id.txtTituloTicket)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcionTicket)
        val txtNombreAutor = findViewById<EditText>(R.id.txtNombreAutorTicket)
        val txtEmail = findViewById<EditText>(R.id.txtEmailAutorTicket)
        val btnAgregar = findViewById<Button>(R.id.btnTicket)
        val rcvTicket = findViewById<RecyclerView>(R.id.rcvTicket)

        rcvTicket.layoutManager = LinearLayoutManager(this)


        fun obtenerTickets(): List<listaTickets> {
            //1- Creo una objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from TB_TICKET")!!

            val listadoTickets = mutableListOf<listaTickets>()

            //Recorrer todos los datos que me trajo el select
            while (resultSet.next()) {
                val numeroTicket = resultSet.getString("NUMERO_DE_TICKET")
                val titulo = resultSet.getString("TITULO")

                val descripcion = resultSet.getString("DESCRIPCION")
                val autor = resultSet.getString("AUTOR")
                val emailAutor = resultSet.getString("EMAIL_AUTOR")
                val fechaCreacion = resultSet.getDate("FECHA_DE_CREACION")
                val estado = resultSet.getString("ESTADO")
                val fechaFinalizacion = resultSet.getDate("FECHA_DE_FINALIZACION")

                val valoresJuntos = listaTickets(numeroTicket, titulo, descripcion, autor, emailAutor, fechaCreacion, estado, fechaFinalizacion
                )
                listadoTickets.add(valoresJuntos)
            }
            return listadoTickets
        }

        //Ejecutamos la funcion
        CoroutineScope(Dispatchers.IO).launch {
            val ticketDB = obtenerTickets()
            withContext(Dispatchers.Main){

                val adapter = Adaptador(ticketDB)
                rcvTicket.adapter = adapter
            }
        }



    }
}