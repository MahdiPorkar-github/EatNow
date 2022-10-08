package com.example.eatnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatnow.databinding.ActivityMainBinding
import com.example.eatnow.databinding.DialogueAddNewItemBinding
import java.util.*
import kotlin.random.Random.Default.nextFloat

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var foodAdapter: FoodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodAdapter = FoodAdapter(FoodGenerator.getFoods())
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
        val dialogueBinding = DialogueAddNewItemBinding.inflate(layoutInflater)
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
}