package com.example.eatnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatnow.databinding.*
import java.util.*

class MainActivity : AppCompatActivity(), FoodAdapter.FoodEvents {
    lateinit var binding: ActivityMainBinding
    private lateinit var foodAdapter: FoodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodAdapter = FoodAdapter(FoodGenerator.getFoods(), this)
        binding.recyclerMain.adapter = foodAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.imgAdd.setOnClickListener {
            addItem()
        }
    }


    private fun addItem() {
        showDialogue()
    }

    private fun showDialogue() {
        val dialogue = AlertDialog.Builder(this).create()
        val dialogueBinding = DialogAddNewItemBinding.inflate(layoutInflater)
        dialogue.setView(dialogueBinding.root)
        dialogue.setCancelable(true)
        dialogue.show()

        dialogueBinding.dialogBtnDone.setOnClickListener {
            // how should i create a new method for this???
            // cause i want to obey solid rules

            val txtName = dialogueBinding.dialogueEdtFoodName.text.toString()
            val txtCity = dialogueBinding.dialogueEdtFoodCity.text.toString()
            val txtPrice = dialogueBinding.dialogueEdtFoodPrice.text.toString()
            val txtDistance = dialogueBinding.dialogueEdtFoodDistance.text.toString()
            val txtRatersCount = (1..150).random()
            val rating = 0f + Random().nextFloat() * (5f) // generates a float in range of 1-5

            val randomForUrl = (0..11).random()
            val urlPic = FoodGenerator.getFoods()[randomForUrl].urlImage


            if (txtName.isNotEmpty() && txtCity.isNotEmpty() && txtPrice.isNotEmpty() && txtDistance.isNotEmpty()) {
                val newFood =
                    Food(txtName, txtPrice, txtDistance, txtCity, urlPic, txtRatersCount, rating)
                foodAdapter.addFood(newFood)
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
                food.txtSubject = dialogBinding.dialogueEdtFoodName.text.toString()
                food.txtPrice = dialogBinding.dialogueEdtFoodPrice.text.toString()
                food.txtDistance = dialogBinding.dialogueEdtFoodDistance.text.toString()
                food.txtCity = dialogBinding.dialogueEdtFoodCity.text.toString()
                foodAdapter.updateFood(position)
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

            foodAdapter.removeFood(food, position)
            dialog.dismiss()
        }
    }
}
