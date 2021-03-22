package com.example

import android.util.Log
import java.net.URL

class Request (private val url : String) {
    fun run(){
        val jsonDataAir = URL(url).readText()
        Log.d(javaClass.simpleName,jsonDataAir)
    }



}