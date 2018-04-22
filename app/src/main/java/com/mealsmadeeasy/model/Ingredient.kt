package com.mealsmadeeasy.model

data class Ingredient(
        val quantity: Float,
        val unit: String,
        val name: String
) {

    constructor(quantity: Number, unit: String, name: String): this(quantity.toFloat(), unit, name)

}
