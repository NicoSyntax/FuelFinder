package de.syntax.androidabschluss.data.model

data class Station (
    val id: String,
    val name: String,
    val brand: String,
    val street: String,
    val place: String,
    val lat: Double,
    val lng: Double,
    val dist: Double,
    val diesel: Double,
    val e5: Double,
    val e10: Double?,
    val isOpen: Boolean,
    val houseNumber: String,
    val postCode: Int,
    var isFavourite: Boolean = false
)
