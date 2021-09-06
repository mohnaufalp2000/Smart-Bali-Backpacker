package com.smart.smartbalibackpaker.model.personalchat

data class DataUser(
    var id: String,
    var username: String,
    var email: String,
    var image: String
) {
    constructor() : this("", "", "", "") {

    }
}
