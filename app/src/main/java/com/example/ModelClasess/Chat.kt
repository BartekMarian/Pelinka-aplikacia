package com.example.ModelClasess

class Chat {
    private var user : String = ""
    private var message : String = ""
    private var mesageId : String = ""
    private var url :String = ""

    constructor()

    constructor(user: String, message: String, mesageId: String, url :String) {
        this.user = user
        this.message = message
        this.mesageId = mesageId
        this.url = url
    }

    fun getUser():String? {
        return user
    }

    fun setUser(user: String?){
        this.user = user!!
    }

    fun getMessage():String? {
        return message
    }

    fun setMessage(user: String?){
        this.message = message!!
    }

    fun getMessageId():String? {
        return mesageId
    }

    fun setMessageId(mesageId: String?){
        this.mesageId = mesageId!!
    }
    fun getUrl():String? {
        return url
    }

    fun setUrl(url: String?){
        this.url = url!!
    }

}