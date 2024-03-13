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
    val price: Double,
    val isOpen: Boolean,
    val houseNumber: String,
    val postCode: Int
)
