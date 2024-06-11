package RecyclerViewHelper

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import gabriel.contreras.segundolaboratoriocrud2b.R
import gabriel.contreras.segundolaboratoriocrud2b.detalle_ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaTickets


class Adaptador(private var Datos: List<listaTickets>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)

        return ViewHolder(vista)
    }
    fun actualizarLista(nuevaLista: List<listaTickets>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
    }

    fun actualizarPantalla(uuid: String, nuevoNombre: String){
        val index = Datos.indexOfFirst { it.numeroTicket == uuid }
        Datos[index].titulo = nuevoNombre
        notifyDataSetChanged()
    }
    /////////////////// TODO: Eliminar datos
    fun eliminarDatos(tituloTicket: String, posicion: Int){
        //Actualizo la lista de datos y notifico al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            //1- Creamos un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Crear una variable que contenga un PrepareStatement
            val borrarTicket = objConexion?.prepareStatement("delete from TB_TICKET where TITULO = ?")!!
            borrarTicket.setString(1, tituloTicket)
            borrarTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        // Notificar al adaptador sobre los cambios
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //////////////////////TODO: Editar datos
    fun actualizarTicket(nuevoTitulo: String, uuid: String){
        GlobalScope.launch(Dispatchers.IO){

            //1- Creo un objeto de la clase de conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //2- creo una variable que contenga un PrepareStatement
            val actualizarTicket = objConexion?.prepareStatement("update TB_TICKET set TITULO = ? where NUMERO_DE_TICKET = ?")!!
            actualizarTicket.setString(1, nuevoTitulo)
            actualizarTicket.setString(2, uuid)
            actualizarTicket.executeUpdate()

            withContext(Dispatchers.Main){
                actualizarPantalla(uuid, nuevoTitulo)
            }

        }
    }
//    fun actualizarDescripcionDetalle(nuevaDescripcion: String, uuid: String){
//        GlobalScope.launch(Dispatchers.IO){
//
//            //1- Creo un objeto de la clase de conexion
//            val objConexion = ClaseConexion().cadenaConexion()
//
//            //2- creo una variable que contenga un PrepareStatement
//            val actualizarTicket = objConexion?.prepareStatement("update TB_TICKET set DESCRIPCION = ? where NUMERO_DE_TICKET = ?")!!
//            actualizarTicket.setString(1, nuevaDescripcion)
//            actualizarTicket.setString(2, uuid)
//            actualizarTicket.executeUpdate()
//
//            withContext(Dispatchers.Main){
//                actualizarPantalla(uuid, nuevaDescripcion)
//            }
//
//        }
//    }
//
//    fun actualizarAutorDetalle(nuevoAutor: String, uuid: String){
//        GlobalScope.launch(Dispatchers.IO){
//
//            //1- Creo un objeto de la clase de conexion
//            val objConexion = ClaseConexion().cadenaConexion()
//
//            //2- creo una variable que contenga un PrepareStatement
//            val actualizarTicket = objConexion?.prepareStatement("update TB_TICKET set AUTOR = ? where NUMERO_DE_TICKET = ?")!!
//            actualizarTicket.setString(1, nuevoAutor)
//            actualizarTicket.setString(2, uuid)
//            actualizarTicket.executeUpdate()
//
//            withContext(Dispatchers.Main){
//                actualizarPantalla(uuid, nuevoAutor)
//            }
//
//        }
//    }
//    fun actualizarEmailDetalle(nuevoEmail: String, uuid: String){
//        GlobalScope.launch(Dispatchers.IO){
//
//            //1- Creo un objeto de la clase de conexion
//            val objConexion = ClaseConexion().cadenaConexion()
//
//            //2- creo una variable que contenga un PrepareStatement
//            val actualizarTicket = objConexion?.prepareStatement("update TB_TICKET set EMAIL_AUTOR = ? where NUMERO_DE_TICKET = ?")!!
//            actualizarTicket.setString(1, nuevoEmail)
//            actualizarTicket.setString(2, uuid)
//            actualizarTicket.executeUpdate()
//
//            withContext(Dispatchers.Main){
//                actualizarPantalla(uuid, nuevoEmail)
//            }
//
//        }
//    }
//



    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    //Controlar a la card
        val ticket = Datos[position]
        holder.txtTextoTicket.text = ticket.titulo

        //todo: clic al icono de eliminar
        holder.imgBorrar.setOnClickListener {

            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el ticket?")

            //Botones
            builder.setPositiveButton("Si") { dialog, which ->
                eliminarDatos(ticket.titulo, position)
            }

            builder.setNegativeButton("No"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

        //Todo: icono de editar
        holder.imgEditar.setOnClickListener{
            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Actualizar")
            builder.setMessage("¿Desea actualizar el ticket (si desea actualizar más datos, dar click al título)?")

            //Agregarle un cuadro de texto para
            //que el usuario escriba el nuevo nombre
            val cuadroTexto = EditText(context)
            cuadroTexto.setHint(ticket.titulo)
            builder.setView(cuadroTexto)

            //Botones
            builder.setPositiveButton("Actualizar") { dialog, which ->
                actualizarTicket(cuadroTexto.text.toString(), ticket.numeroTicket)
            }

            builder.setNegativeButton("Cancelar"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

//        holder.txtTextoDescripcion.setOnClickListener{
//            //Creamos un Alert Dialog
//            val context = holder.itemView.context
//
//            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
//            builder.setTitle("Actualizar")
//            builder.setMessage("¿Desea actualizar la descripción del ticket)?")
//
//            //Agregarle un cuadro de texto para
//            //que el usuario escriba la nueva descripción
//            val cuadroTexto = EditText(context)
//            cuadroTexto.setHint(ticket.descripcion)
//            builder.setView(cuadroTexto)
//
//            //Botones
//            builder.setPositiveButton("Actualizar") { dialog, which ->
//                actualizarDescripcionDetalle(cuadroTexto.text.toString(), ticket.numeroTicket)
//            }
//
//            builder.setNegativeButton("Cancelar"){dialog, which ->
//                dialog.dismiss()
//            }
//
//            val dialog = builder.create()
//            dialog.show()
//        }
//
//        holder.txtTextoAutor.setOnClickListener{
//            //Creamos un Alert Dialog
//            val context = holder.itemView.context
//
//            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
//            builder.setTitle("Actualizar")
//            builder.setMessage("¿Desea actualizar al Autor del ticket)?")
//
//            //Agregarle un cuadro de texto para
//            //que el usuario escriba al nuevo autor
//            val cuadroTexto = EditText(context)
//            cuadroTexto.setHint(ticket.autor)
//            builder.setView(cuadroTexto)
//
//            //Botones
//            builder.setPositiveButton("Actualizar") { dialog, which ->
//                actualizarAutorDetalle(cuadroTexto.text.toString(), ticket.numeroTicket)
//            }
//
//            builder.setNegativeButton("Cancelar"){dialog, which ->
//                dialog.dismiss()
//            }
//
//            val dialog = builder.create()
//            dialog.show()
//        }
//        holder.txtTextoEmailAutor.setOnClickListener{
//            //Creamos un Alert Dialog
//            val context = holder.itemView.context
//
//            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
//            builder.setTitle("Actualizar")
//            builder.setMessage("¿Desea actualizar el Email del autor del ticket)?")
//
//            //Agregarle un cuadro de texto para
//            //que el usuario escriba al nuevo autor
//            val cuadroTexto = EditText(context)
//            cuadroTexto.setHint(ticket.autor)
//            builder.setView(cuadroTexto)
//
//            //Botones
//            builder.setPositiveButton("Actualizar") { dialog, which ->
//                actualizarEmailDetalle(cuadroTexto.text.toString(), ticket.numeroTicket)
//            }
//
//            builder.setNegativeButton("Cancelar"){dialog, which ->
//                dialog.dismiss()
//            }
//
//            val dialog = builder.create()
//            dialog.show()
//        }

        //Todo: Clic a la card completa
        //Vamos a ir a otra pantalla donde me mostrará todos los datos
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            //Cambiar de pantalla a la pantalla de detalle
            val pantallaDetalle = Intent(context, detalle_ticket::class.java)
            //enviar a la otra pantalla todos mis valores
            pantallaDetalle.putExtra("NUMERO_DE_TICKET", ticket.numeroTicket)
            pantallaDetalle.putExtra("TITULO", ticket.titulo)
            pantallaDetalle.putExtra("DESCRIPCION", ticket.descripcion)
            pantallaDetalle.putExtra("AUTOR", ticket.autor)
            pantallaDetalle.putExtra("EMAIL_AUTOR", ticket.email)
            pantallaDetalle.putExtra("FECHA_DE_CREACION", ticket.fecha)
            pantallaDetalle.putExtra("ESTADO", ticket.estado)

            context.startActivity(pantallaDetalle)
        }
    }

}
