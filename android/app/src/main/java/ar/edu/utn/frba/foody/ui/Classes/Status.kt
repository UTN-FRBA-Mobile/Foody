package ar.edu.utn.frba.foody.ui.Classes

enum class Status(val description: String) {
    INPROGRESS("En Progreso"),
    PENDING("Pendiente"),
    ONTHEWAY("En Camino"),
    FINALIZED("Finalizado")
}