package com.example.eatnow.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,

    var txtSubject: String,
    var txtPrice: String,
    var txtDistance: String,
    var txtCity: String,
    val urlImage: String,
    val ratersCount: Int,
    val rating: Float
)
