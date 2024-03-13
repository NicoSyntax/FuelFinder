package de.syntax.androidabschluss.data.model

data class ServerResponse(
    val ok: Boolean,
    val license: String,
    val data: String,
    val status: String,
    val stations: List<Station>
)
