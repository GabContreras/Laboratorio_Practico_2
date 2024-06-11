package RecyclerViewHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gabriel.contreras.segundolaboratoriocrud2b.R

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtTextoTicket: TextView = view.findViewById(R.id.txtTicketCard)
//    val txtTextoDescripcion: TextView = view.findViewById(R.id.txtDescripcionDetalle)
//    val txtTextoAutor: TextView = view.findViewById(R.id.txtAutorDetalle)
//    val txtTextoEmailAutor: TextView = view.findViewById(R.id.txtEmailAutorDetalle)

    val imgEditar: ImageView = view.findViewById(R.id.imgBorrar)
    val imgBorrar: ImageView = view.findViewById(R.id.imgEditar)
}
