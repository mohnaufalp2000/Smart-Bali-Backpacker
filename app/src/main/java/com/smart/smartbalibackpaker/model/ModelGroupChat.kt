package com.smart.smartbalibackpaker.model

data class ModelGroupChat(
    var groupId : String = "",
    var groupTitle : String = "",
    var time : String = "",
    var createdBy : String = ""
) {
    constructor() : this("", "", "", "") {

    }
}