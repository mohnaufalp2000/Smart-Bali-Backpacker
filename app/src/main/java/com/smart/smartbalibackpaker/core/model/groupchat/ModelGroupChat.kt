package com.smart.smartbalibackpaker.core.model.groupchat

data class ModelGroupChat (
    val message : String = "",
    val sender : String = "",
    val time : String = "",
    val type : String = ""
    ) {
    constructor() : this("", "", "", "")
}