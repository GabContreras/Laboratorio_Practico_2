package modelo

import java.util.Date

data class listaTickets (
    val numeroTicket: String,
    val titulo: String,
    val descripcion: String,
    val autor: String,
    val email: String,
    val fecha: Date,
    val estado: String,
    val fechaFinalizacion: Date



)