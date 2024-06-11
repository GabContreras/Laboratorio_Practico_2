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
import java.sql.Date
import java.util.Calendar

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
        // Mandar a llamar todos los elementos de la vista
        val txtTitulo = findViewById<EditText>(R.id.txtTituloTicket)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcionTicket)
        val txtNombreAutor = findViewById<EditText>(R.id.txtNombreAutorTicket)
        val txtEmail = findViewById<EditText>(R.id.txtEmailAutorTicket)

        val fechaActual = Date(Calendar.getInstance().time.time)

        val btnAgregar = findViewById<Button>(R.id.btnTicket)
        val rcvTicket = findViewById<RecyclerView>(R.id.rcvTicket)

        rcvTicket.layoutManager = LinearLayoutManager(this)

        fun obtenerTickets(): List<listaTickets> {
            val listadoTickets = mutableListOf<listaTickets>()

            // Crear un objeto de la clase conexión
            val objConexion = ClaseConexion().cadenaConexion()

            //2 Crear un statement
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT NUMERO_DE_TICKET, TITULO, DESCRIPCION, AUTOR, EMAIL_AUTOR, FECHA_DE_CREACION, ESTADO FROM TB_TICKET")!!
            // Recorrer todos los datos que me trajo el select

            val listaTickets = mutableListOf<listaTickets>()

            while (resultSet.next()) {
                val numeroTicket = resultSet.getString("NUMERO_DE_TICKET")
                val titulo = resultSet.getString("TITULO")
                val descripcion = resultSet.getString("DESCRIPCION")
                val autor = resultSet.getString("AUTOR")
                val emailAutor = resultSet.getString("EMAIL_AUTOR")
                val fechaCreacion = resultSet.getDate("FECHA_DE_CREACION")
                val estado = resultSet.getString("ESTADO")

                val valoresJuntos = listaTickets(numeroTicket,titulo,descripcion,autor,
                    emailAutor,fechaCreacion,estado)
                listadoTickets.add(valoresJuntos)
            }
            return listadoTickets

        }

        // Ejecutamos la función
        CoroutineScope(Dispatchers.IO).launch {
            val ticketDB = obtenerTickets()
            withContext(Dispatchers.Main) {
                val adapter = Adaptador(ticketDB)
                rcvTicket.adapter = adapter
            }
        }

        //Botón para agregar tickets (ya sirve)
        btnAgregar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                    // Crear un objeto de la clase conexión
                    val objConexion = ClaseConexion().cadenaConexion() ?: throw NullPointerException("Error al obtener la conexión de la base de datos")

                    // Obtener la fecha actual
                    val currentDate = Date(Calendar.getInstance().time.time)

                    // Crear una variable que contenga un PrepareStatement
                    val addTicket = objConexion.prepareStatement("INSERT INTO TB_TICKET(NUMERO_DE_TICKET, TITULO, DESCRIPCION, AUTOR, EMAIL_AUTOR, FECHA_DE_CREACION, ESTADO ) VALUES (? , ? , ? , ? , ? ,?,'Activo')")!!
                    addTicket.setString(1, UUID.randomUUID().toString())
                    addTicket.setString(2, txtTitulo.text.toString())
                    addTicket.setString(3, txtDescripcion.text.toString())
                    addTicket.setString(4, txtNombreAutor.text.toString())
                    addTicket.setString(5, txtEmail.text.toString())
                    addTicket.setDate(6, currentDate)
                    addTicket.executeUpdate()

                    // Refrescar la lista
                    val nuevoTicket = obtenerTickets()
                    withContext(Dispatchers.Main) {
                        (rcvTicket.adapter as? Adaptador)?.actualizarLista(nuevoTicket)
                    }

            }
        }
    }
}