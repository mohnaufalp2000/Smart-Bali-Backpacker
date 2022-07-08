package com.smart.smartbalibackpaker.guide


interface GetData{
    fun onGetData(data: Int?)
}

interface GetTrafficJamData{
    fun onGetTrafficJamData(expectedDuration: Int, durationInTraffic: Int)
}