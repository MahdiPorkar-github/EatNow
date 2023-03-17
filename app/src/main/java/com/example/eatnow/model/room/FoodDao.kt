package com.example.eatnow.model.room

import androidx.room.*

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(food: Food)

    @Insert
    fun insertAllFoods(data : List<Food>)

    @Delete
    fun deleteFood(food: Food)

    @Query("DELETE FROM food_table")
    fun deleteAllFoods()

    @Query("SELECT * FROM food_table")
    fun getAllFoods(): List<Food>

    @Query("SELECT * FROM food_table WHERE txtSubject LIKE '%' || :input || '%'")
    fun searchFood(input: String): List<Food>

}