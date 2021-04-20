package com.example.ModelClasess

class Users {
    private var uid:String = ""
    private var cover:String = ""
    private var profile:String = ""
    private var username:String = ""
    private var phone:String = ""
    private var useremail:String = ""
    private var password:String = ""
    private var switch3:Boolean = true

    constructor()

    constructor(
        uid: String,
        cover: String,
        profile: String,
        username: String,
        phone: String,
        useremail: String,
        password: String,
        switch3:Boolean
    ) {
        this.uid = uid
        this.cover = cover
        this.profile = profile
        this.username = username
        this.phone = phone
        this.useremail = useremail
        this.password = password
        this.switch3 = switch3
    }


    fun getUID(): String? {
        return uid
    }
    fun setUID(uid:String){
        this.uid = uid
    }

    fun getCover(): String? {
        return cover
    }
    fun setCover(cover: String){
        this.cover = cover
    }

    fun getProfile(): String? {
        return profile
    }
    fun setProfile(profile: String){
        this.profile = profile
    }

    fun getUserName(): String? {
        return username
    }
    fun setUserName(username:String){
        this.username = username
    }

    fun getPhone(): String? {
        return phone
    }
    fun setPhone(phone:String){
        this.phone = phone
    }

    fun getUserEmail(): String? {
        return useremail
    }
    fun setUserEmail(useremail:String){
        this.useremail = useremail
    }

    fun getPassword(): String? {
        return password
    }
    fun setPassword(password:String){
        this.password = password
    }
    fun getSwitch3(): Boolean {
        return switch3
    }
    fun setSwitch3(switch3: Boolean){
        this.switch3 = switch3
    }
}