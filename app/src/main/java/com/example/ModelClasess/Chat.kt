package com.example.ModelClasess

class Chat {
    private var senderId : String = ""
    private var message : String = ""
    private var mesageId : String = ""
    private var url :String = ""
    private var sender_profile = ""
    private var timestamp: Double? = null
    private var timestamp1: Double? = null
    private var connection :String = ""

    constructor()

    constructor(senderId: String, message: String, mesageId: String, url :String,timestamp1: Double, timestamp: Double, sender_profile :String) {
        this.senderId = senderId
        this.message = message
        this.mesageId = mesageId
        this.url = url
        this.timestamp = timestamp
        this.timestamp1 = timestamp1
        this.sender_profile
        this.connection
    }

    fun getConnection():String?{
        return connection
    }

    fun setConnection(connection: String?){
        this.connection = connection!!
    }

    fun getSender_profile():String?{
        return sender_profile
    }

    fun setSender_profile(sender_profile: String?){
        this.sender_profile = sender_profile!!
    }
    fun getTimestamp1():Double?{
        return timestamp1
    }
    fun setTimestamp1(timestamp1: Double?){
        this.timestamp1 = timestamp1!!
    }

    fun getTimestamp():Double?{
        return timestamp
    }
    fun setTimestamp(timestamp: Double?){
        this.timestamp = timestamp!!
    }

    fun getUser():String? {
        return senderId
    }

    fun setUser(senderId: String?){
        this.senderId = senderId!!
    }

    fun getMessage():String? {
        return message
    }
    fun setMessage() {
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