package modelo

import java.util.Date

data class listaTickets (
    val numeroTicket: String,
    var titulo: String,
    var descripcion: String,
    var autor: String,
    var email: String,
    var fecha: Date,
    var estado: String,
    )