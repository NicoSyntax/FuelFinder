package de.syntax.androidabschluss.data.model

data class ApiResponse(
    val ok: Boolean,
    val license: String,
    val data: String,
    val status: String,
    var stations: List<Station>
)