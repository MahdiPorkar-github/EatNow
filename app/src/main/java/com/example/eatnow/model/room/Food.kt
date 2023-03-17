package com.example.eatnow.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey


/*
In an application with a good layered architecture, this model would only be the gateway to the domain layer or business logic.
See it as the provider of the data we want to display in the view. Model’s responsibilities include using APIs, caching data,
managing databases and so on.
 */
@Entity(tableName = "food_table")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    var txtSubject: String,
    var txtPrice: String,
    var txtDistance: String,
    var txtCity: String,
    val drawable: Int,
    val ratersCount: Int,
    val rating: Float
)
