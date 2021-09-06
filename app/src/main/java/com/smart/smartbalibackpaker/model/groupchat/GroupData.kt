package com.smart.smartbalibackpaker.model.groupchat

data class GroupData(
    var groupId : String = "",
    var groupTitle : String = "",
    var groupImage : String = "",
    var time : String = "",
    var createdBy : String = ""
) {
    constructor() : this("", "", "", "", "") {

    }
}