package com.smart.smartbalibackpaker.guide

import android.util.Log
import com.smart.smartbalibackpaker.core.data.source.local.entity.GuideMapsEntity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object FloydWarshall {
    private var k = 0
    private val listPlace = ArrayList<GuideMapsEntity?>()
    private var list = ArrayList<Int?>()
    private val anestedLoopIndex = ArrayList<Int?>()
//    private val tableNode = Array(listPlace.size+1){Array(2){""} }

    fun getDistance(valueList : ArrayList<Int?>, listPlace: ArrayList<GuideMapsEntity>, node: HashMap<Any, Int?>){
        k = listPlace.size
        Log.d("ksize", k.toString())
        this.listPlace.clear()
        this.listPlace.addAll(listPlace)
        Log.d("nodestring", node.toString())
        Log.d("thislist", this.listPlace.toString())

//      sort node
        val sortedNode : MutableMap<Any, Int?> = TreeMap(node)
        Log.d("sortednode", sortedNode.toString())

//      get node values
        list.clear()
        list = ArrayList(sortedNode.values)
        Log.d("listlist", list.toString())

//        if(k == (list.size - k) - ((list.size - k) - k)){
//            createTable()
//        }

        if(k*3 == list.size){
            createTable()
        }
    }

    private fun createTable() {
        val table = Array(k) { IntArray(k) }
        var indexTable = 0

//      create table and insert distance value to table
        for(row in 0 until k){
            for (column in 0 until k){
                if(row == column){
                    table[row][column] = 9999
                } else {
                    table[row][column] = list[indexTable]!!
                }

                if(indexTable == list.size - 1){
                    indexTable = 0
                } else {
                    if(row != column){
                        indexTable++
                    }
                }
            }
        }

//      print table
        for(column in table){
            for(value in column){
                print(value)
                print(" ")
            }
            println("")
        }
        println("test")

//      floyd-warshall
        val anestedLoop = ArrayList<Int?>()
        anestedLoopIndex.add(1)
        val tableValues = ArrayList<Int>()
        val tableValuesSorted = ArrayList<Int>()
        var tableRow = 0
        var tableColumn = 0
        var idx = 0
        val columnIteration = ArrayList<Int>()

        while(idx < k) {
            tableValues.add(table[tableRow][tableColumn])
            tableColumn++
            if (tableValues.size != 4) continue
            tableColumn = 0
            tableValuesSorted.addAll(tableValues)

//          Bubble sort
            for(i in 0 until tableValuesSorted.size-1){
                for(j in 0 until tableValuesSorted.size-i-1){
                    if(tableValuesSorted[j] > tableValuesSorted[j+1]){
                        val tmp = tableValuesSorted[j]
                        tableValuesSorted[j] = tableValuesSorted[j+1]
                        tableValuesSorted[j+1] = tmp
                    }
                }
            }
            Log.d("tableValuesSorted", tableValuesSorted.toString())

            var minValue = 0
            if(tableValuesSorted[0] < 9999){
                minValue = tableValuesSorted[0]
            } else {
                minValue = tableValuesSorted.find { item -> item > 9999 } ?: 0
            }

            Log.d("minvalue", minValue.toString())

//          get index of min value
            val minValueIndex = tableValues.indexOf(minValue)
            anestedLoopIndex.add(minValueIndex+1)

            tableValuesSorted.clear()
            tableValues.clear()

            Log.d("distancefloyd", anestedLoop.toString())
            Log.d("distancefloydIndex", anestedLoopIndex.toString())

            for(column in 0 until k){
                if (table[tableRow][column] == minValue) {
                    table[tableRow][column] = 9999
                    tableRow = column
                    columnIteration.add(column)
                    Log.d("columnite", columnIteration.toString())
                    columnIteration.forEachIndexed { index,_ ->
                        if(index == 0){
                            table[columnIteration[index]][0] = 9999
                        } else {
                            table[columnIteration[index]][0] = 9999
                            table[columnIteration[index]][columnIteration[index-1]] = 9999
                        }
                    }
                    tableColumn = 0
                    idx++
                }
            }
        }

        defineRoutes()

//      print table
        for(column in table){
            for(value in column){
                print(value)
                print(" ")
            }
            println("")
        }
    }

    fun defineRoutes() : MutableList<GuideMapsEntity?>{
        val orderByIndex = listPlace.associateBy { it?.id }
        val sortedNode = mutableListOf<GuideMapsEntity?>()
        anestedLoopIndex.forEachIndexed { index, value ->
            if(index < k){
                sortedNode.add(orderByIndex[value])
            }
        }
        Log.d("sortednodema", sortedNode.toString())
        return sortedNode
    }
}