package com.example.eatnow

import androidx.room.Entity

@Entity
data class Food(
    var txtSubject: String,
    var txtPrice: String,
    var txtDistance: String,
    var txtCity: String,
    val urlImage: String,
    val ratersCount: Int,
    val rating: Float
)
