package com.example.eatnow.utils

interface BasePresenter<V> {
    fun onAttach(view: V)
    fun onDetach()
}