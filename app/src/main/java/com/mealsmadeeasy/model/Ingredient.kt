package com.mealsmadeeasy.model

data class Ingredient (
        val id: String,
        val name: String,
        val quantity: Double,
        val unitName: String,
        val isMeasurable: Boolean = true
) {

    constructor(id: String, name: String, quantity: Int, unitName: String, isMeasurable: Boolean = true):
            this(id, name, quantity.toDouble(), unitName, isMeasurable)
}
