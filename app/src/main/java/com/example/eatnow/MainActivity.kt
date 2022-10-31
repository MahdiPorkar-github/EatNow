package com.example.eatnow

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatnow.databinding.*
import com.example.eatnow.room.Food
import com.example.eatnow.room.FoodDao
import com.example.eatnow.room.FoodDatabase
import java.util.*
import kotlin.collections.ArrayList


const val BASE_URL_IMAGE = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food"

class MainActivity : AppCompatActivity(), FoodAdapter.FoodEvents {

    lateinit var binding: ActivityMainBinding
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var foodDao: FoodDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodDao = FoodDatabase.getDatabase(this).foodDao

        val sharedPreferences = getSharedPreferences("eatNow", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("firstRun", true)) {
            firstRun()
            sharedPreferences.edit().putBoolean("firstRun", false).apply()
        }

        showAllData()


        binding.imgAdd.setOnClickListener {
            addNewFood()
        }

        binding.imgRemoveAll.setOnClickListener {
            removeAllData()
        }

        binding.edtSearchFood.addTextChangedListener { editTextInput ->

            searchOnDatabase(editTextInput.toString())

            if (editTextInput.toString().isNotEmpty()) {

                val filteredList = FoodGenerator.getFoods().filter { food ->
                    food.txtSubject.contains(editTextInput.toString())
                }

                foodAdapter.setData(filteredList as ArrayList<Food>)

            } else {
                foodAdapter.setData(FoodGenerator.getFoods())
                // show all data
            }

        }


    }

    private fun searchOnDatabase(editTextInput: String) {

        if (editTextInput.isNotEmpty()) {

            val filteredList = foodDao.searchFood(editTextInput)
            foodAdapter.setData(filteredList as ArrayList<Food>)

        } else {
            val foodData = foodDao.getAllFoods()
            foodAdapter.setData(ArrayList(foodData))
        }
    }

    private fun removeAllData() {
        foodDao.deleteAllFoods()
        showAllData()
    }

    private fun showAllData() {

        val foodData = foodDao.getAllFoods()
        foodAdapter = FoodAdapter(ArrayList(foodData), this)
        binding.recyclerMain.adapter = foodAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


    }

    private fun firstRun() {
        foodDao.insertAllFoods(FoodGenerator.getFoods())
    }

    private fun addNewFood() {

        val dialogue = AlertDialog.Builder(this).create()
        val dialogueBinding = DialogAddNewItemBinding.inflate(layoutInflater)
        dialogue.setView(dialogueBinding.root)
        dialogue.setCancelable(true)
        dialogue.show()

        dialogueBinding.dialogBtnDone.setOnClickListener {

            val txtName = dialogueBinding.dialogueEdtFoodName.text.toString()
            val txtCity = dialogueBinding.dialogueEdtFoodCity.text.toString()
            val txtPrice = dialogueBinding.dialogueEdtFoodPrice.text.toString()
            val txtDistance = dialogueBinding.dialogueEdtFoodDistance.text.toString()
            val txtRatersCount = (1..150).random()
            val rating = 0f + Random().nextFloat() * (5f) // generates a float in range of 1-5


            val randomForUrl = (1..12).random()
            val urlPic = "$BASE_URL_IMAGE$randomForUrl.jpg"


            if (txtName.isNotEmpty() && txtCity.isNotEmpty() && txtPrice.isNotEmpty() && txtDistance.isNotEmpty()) {
                val newFood =
                    Food(
                        txtSubject = txtName,
                        txtPrice = txtPrice,
                        txtDistance = txtDistance,
                        txtCity = txtCity,
                        urlImage = urlPic,
                        ratersCount = txtRatersCount,
                        rating = rating
                    )
                foodAdapter.addFood(newFood)
                foodDao.insertOrUpdate(newFood)
                binding.recyclerMain.scrollToPosition(0)
                dialogue.dismiss()

            } else {
                Toast.makeText(this, "Complete all the information", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onFoodClicked(food: Food, position: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogUpdateItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.dialogueEdtFoodName.setText(food.txtSubject)
        dialogBinding.dialogueEdtFoodCity.setText(food.txtCity)
        dialogBinding.dialogueEdtFoodDistance.setText(food.txtDistance)
        dialogBinding.dialogueEdtFoodPrice.setText(food.txtPrice)

        dialogBinding.dialogBtnUpdateCancel.setOnClickListener { dialog.dismiss() }
        dialogBinding.dialogBtnUpdateSure.setOnClickListener {

            val txtName = dialogBinding.dialogueEdtFoodName.text.toString()
            val txtCity = dialogBinding.dialogueEdtFoodCity.text.toString()
            val txtPrice = dialogBinding.dialogueEdtFoodPrice.text.toString()
            val txtDistance = dialogBinding.dialogueEdtFoodDistance.text.toString()


            if (txtName.isNotEmpty() && txtCity.isNotEmpty() && txtPrice.isNotEmpty() && txtDistance.isNotEmpty()) {
                val newFood = Food(
                    id = food.id,
                    txtSubject = txtName,
                    txtPrice = txtPrice,
                    txtDistance = txtDistance,
                    txtCity = txtCity,
                    urlImage = food.urlImage,
                    ratersCount = food.ratersCount,
                    rating = food.rating
                )
                foodAdapter.updateFood(newFood, position)
                foodDao.insertOrUpdate(newFood)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onFoodLongClicked(food: Food, position: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.dialogBtnDeleteCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.dialogBtnDeleteSure.setOnClickListener {

            dialog.dismiss()
            foodAdapter.removeFood(food, position)
            foodDao.deleteFood(food)
        }
    }
}
