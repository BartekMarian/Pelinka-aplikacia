package com.example.ModelClasess

class Data {
    private var o2 : Float? = null
    private var name : String? = ""

    constructor()


    constructor(o2: Float?, name : String?) {
        this.o2 = o2
        this.name = name
    }

    fun getO2():Float?{
        return o2
    }

    fun setO2(o2: Float?){
        this.o2 = o2!!
    }

    fun getName():String?{
        return name
    }

    fun setName(name: String?){
        this.name = name!!
    }
}