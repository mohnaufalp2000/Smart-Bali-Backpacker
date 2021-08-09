package com.smart.smartbalibackpaker.model

data class DataUser(
    var id: String,
    var username: String,
    var email: String,
    var image: String
) {
    constructor() : this("", "", "", "") {

    }
}
