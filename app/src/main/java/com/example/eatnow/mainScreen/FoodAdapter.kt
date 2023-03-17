package com.example.eatnow.mainScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatnow.databinding.RowRecyclerFoodBinding
import com.example.eatnow.model.room.Food
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class FoodAdapter(private val foods: ArrayList<Food>, private val foodEvents: FoodEvents) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: RowRecyclerFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {



        fun bindData(food: Food) {

            binding.rowTxtFoodName.text = food.txtSubject
            binding.rowTxtFoodCity.text = food.txtCity
            binding.rowTxtFoodDistance.text = food.txtDistance + " miles from you"
            binding.rowTxtFoodPrice.text = "$" + food.txtPrice + " vip"
            binding.rowFoodRatingBar.rating = food.rating
            binding.rowFoodRatersCount.text = "( ${food.ratersCount} Ratings)"

            Glide.with(binding.root.context).load(food.drawable)
                .transform(RoundedCornersTransformation(16, 4)).into(binding.rowImgMain)

            binding.root.setOnClickListener {
                foodEvents.onFoodClicked(food,adapterPosition)
            }
            binding.root.setOnLongClickListener {
                foodEvents.onFoodLongClicked(food,adapterPosition)
                true
            }

        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowRecyclerFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(foods[position])
    }

    override fun getItemCount() = foods.size


    fun addFood(newFood: Food) {
        foods.add(0, newFood)
        notifyItemInserted(0)
    }

    fun removeFood(oldFood: Food, oldPosition: Int) {
        foods.remove(oldFood)
        notifyItemRemoved(oldPosition)
    }


    fun updateFood(newFood: Food, position: Int) {
        foods[position] = newFood
        notifyItemChanged(position)
    }

    fun setData(newList : ArrayList<Food>) {
        foods.clear()
        foods.addAll(newList)
        notifyDataSetChanged()
    }



    interface FoodEvents {
        fun onFoodClicked(food: Food, position: Int)
        fun onFoodLongClicked(food: Food, position: Int)
    }

}