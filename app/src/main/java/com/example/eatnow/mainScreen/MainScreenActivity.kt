package com.example.eatnow.mainScreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatnow.databinding.*
import com.example.eatnow.model.room.Food
import com.example.eatnow.model.room.FoodDao
import com.example.eatnow.model.room.FoodDatabase
import com.example.eatnow.utils.showToast
import java.util.*
import kotlin.collections.ArrayList


class MainScreenActivity : AppCompatActivity(), FoodAdapter.FoodEvents, MainScreenContract.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var foodDao: FoodDao
    private lateinit var mainScreenPresenter: MainScreenContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainScreenPresenter = MainScreenPresenter(FoodDatabase.getDatabase(this).foodDao)
        mainScreenPresenter.onAttach(this)

        foodDao = FoodDatabase.getDatabase(this).foodDao

        val sharedPreferences = getSharedPreferences("eatNow", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("firstRun", true)) {
            mainScreenPresenter.firstRun()
            sharedPreferences.edit().putBoolean("firstRun", false).apply()
        }

        mainScreenPresenter.onAttach(this)

        showAllData()


        binding.imgAdd.setOnClickListener {
            addNewFood()
        }

        binding.imgRemoveAll.setOnClickListener {
            mainScreenPresenter.onDeleteAllClicked()
        }

        binding.edtSearchFood.addTextChangedListener { editTextInput ->
            mainScreenPresenter.onSearchFood(editTextInput.toString())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainScreenPresenter.onDetach()
    }


    private fun showAllData() {

        val foodData = foodDao.getAllFoods()
        foodAdapter = FoodAdapter(ArrayList(foodData), this)
        binding.recyclerMain.adapter = foodAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


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

            val randomForUrl = (0..8).random()
            val urlPic = foodDao.getAllFoods()[randomForUrl].drawable


            if (txtName.isNotEmpty() && txtCity.isNotEmpty() && txtPrice.isNotEmpty() && txtDistance.isNotEmpty()) {
                val newFood =
                    Food(
                        txtSubject = txtName,
                        txtPrice = txtPrice,
                        txtDistance = txtDistance,
                        txtCity = txtCity,
                        drawable = urlPic,
                        ratersCount = txtRatersCount,
                        rating = rating
                    )
                mainScreenPresenter.onAddNewFoodClicked(newFood)
                dialogue.dismiss()

            } else {
                showToast("Complete all fields  ")
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
                    drawable = food.drawable,
                    ratersCount = food.ratersCount,
                    rating = food.rating
                )
                mainScreenPresenter.onUpdateFood(newFood, position)
                dialog.dismiss()
            } else {
                showToast("Please fill all the fields")
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

            mainScreenPresenter.onDeleteFood(food, position)
            dialog.dismiss()
        }
    }

    override fun showFoods(data: List<Food>) {
        foodAdapter = FoodAdapter(ArrayList(data), this)
        binding.recyclerMain.adapter = foodAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun showRefreshedFoods(data: List<Food>) {
        foodAdapter.setData(ArrayList(data))
    }

    override fun showNewFoodAdded(newFood: Food) {
        foodAdapter.addFood(newFood)
    }

    override fun showDeletedFoodList(oldFood: Food, position: Int) {
        foodAdapter.removeFood(oldFood, position)
    }

    override fun showUpdatedFood(editingFood: Food, position: Int) {
        foodAdapter.updateFood(editingFood, position)
    }
}
