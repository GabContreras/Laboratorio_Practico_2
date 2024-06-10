package RecyclerViewHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gabriel.contreras.segundolaboratoriocrud2b.R

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtTextoTicket: TextView = view.findViewById(R.id.txtTicketCard)
    val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
    val imgBorrar: ImageView = view.findViewById(R.id.imgBorrar)
}
