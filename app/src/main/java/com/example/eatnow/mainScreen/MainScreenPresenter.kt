package com.example.eatnow.mainScreen

import com.example.eatnow.R
import com.example.eatnow.model.room.Food
import com.example.eatnow.model.room.FoodDao

class MainScreenPresenter(private val foodDao: FoodDao) : MainScreenContract.Presenter {

    private var mainView: MainScreenContract.View? = null

    override fun firstRun() {
        val pic = R.drawable.pizza

        val firstRunFoodList = listOf(
            Food(
                drawable = R.drawable.pizza,
                txtSubject = "pizza",
                txtCity = "Rome",
                txtPrice = "25",
                txtDistance = "1.6 miles from you",
                rating = 4.7f,
                ratersCount = 1500,
            ),
            Food(
                drawable = R.drawable.fried_chicken,
                txtSubject = "Fried Chicken",
                txtCity = "New York",
                txtPrice = "15",
                txtDistance = "3.6 miles from you",
                rating = 4.1f,
                ratersCount = 500
            ),

            Food(
                drawable = R.drawable.sushi,
                txtSubject = "Sushi",
                txtCity = "Tokyo",
                txtPrice = "13",
                txtDistance = "2.6 miles from you",
                rating = 3.9f,
                ratersCount = 700
            ),
            Food(
                drawable = R.drawable.bonab_kebab,
                txtSubject = "Kebab",
                txtCity = "Bonab",
                txtPrice = "33",
                txtDistance = "2.6 miles from you",
                rating = 4.9f,
                ratersCount = 900
            ),
            Food(
                drawable = R.drawable.cesar_salad,
                txtSubject = "Caesar Salad",
                txtCity = "Mexico",
                txtPrice = "9",
                txtDistance = "2.0 miles from you",
                rating = 4.2f,
                ratersCount = 300
            ),

            Food(
                drawable = R.drawable.hamburger,
                txtSubject = "Hamburger",
                txtCity = "Mexico",
                txtPrice = "19",
                txtDistance = "3.2 miles from you",
                rating = 4.6f,
                ratersCount = 1200
            ),
            Food(
                drawable = R.drawable.lazania,
                txtSubject = "Lazania",
                txtCity = "Naples",
                txtPrice = "19",
                txtDistance = "3.2 miles from you",
                rating = 3.6f,
                ratersCount = 200
            ),
            Food(
                drawable = R.drawable.kufte,
                txtSubject = "Kufte",
                txtCity = "Tabriz",
                txtPrice = "10",
                txtDistance = "2.2 miles from you",
                rating = 3.8f,
                ratersCount = 200
            ),
        )
        foodDao.insertAllFoods(firstRunFoodList)
    }

    override fun onAttach(view: MainScreenContract.View) {
        mainView = view
        mainView!!.showFoods(foodDao.getAllFoods())
    }

    override fun onDetach() {
        mainView = null
    }

    override fun onSearchFood(filter: String) {
        if (filter.isNotEmpty()) {
            // show filtered data
            val dataToShow = foodDao.searchFood(filter)
            mainView!!.showRefreshedFoods(dataToShow)

        } else {
            // show all data
            val dataToShow = foodDao.getAllFoods()
            mainView!!.showRefreshedFoods(dataToShow)
        }
    }

    override fun onAddNewFoodClicked(food: Food) {
        foodDao.insertOrUpdate(food)
        mainView!!.showNewFoodAdded(food)
    }

    override fun onDeleteAllClicked() {
        foodDao.deleteAllFoods()
        mainView!!.showRefreshedFoods(foodDao.getAllFoods())
    }

    override fun onUpdateFood(food: Food, position: Int) {
        foodDao.insertOrUpdate(food)
        mainView!!.showUpdatedFood(food, position)
    }

    override fun onDeleteFood(food: Food, position: Int) {
        foodDao.deleteFood(food)
        mainView!!.showDeletedFoodList(food, position)
    }
}