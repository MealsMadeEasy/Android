package com.mealsmadeeasy.model

data class Ingredient (
        val id: String,
        val name: String,
        val quantity: Double,
        val unitName: String
) {

    constructor(id: String, name: String, quantity: Int, unitName: String):
            this(id, name, quantity.toDouble(), unitName)

    override fun toString(): String {
        return name + " " + quantity
    }
}
