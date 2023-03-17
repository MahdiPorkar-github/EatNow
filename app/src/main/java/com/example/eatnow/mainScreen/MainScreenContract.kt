package com.example.eatnow.mainScreen

import com.example.eatnow.model.room.Food
import com.example.eatnow.utils.BasePresenter

interface MainScreenContract {

    interface Presenter : BasePresenter<MainScreenContract.View> {

        fun firstRun()
        fun onSearchFood(filter: String)
        fun onAddNewFoodClicked(food: Food)
        fun onDeleteAllClicked()
        fun onUpdateFood(food: Food, position: Int)
        fun onDeleteFood(food: Food, position: Int)
    }

    interface View {

        fun showFoods(data: List<Food>)
        fun showRefreshedFoods(data: List<Food>)
        fun showNewFoodAdded(newFood: Food)
        fun showUpdatedFood(editingFood: Food, position: Int)
        fun showDeletedFoodList(oldFood: Food, position: Int)
    }

}