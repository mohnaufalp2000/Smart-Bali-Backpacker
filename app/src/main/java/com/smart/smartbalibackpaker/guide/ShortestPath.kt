package com.smart.smartbalibackpaker.guide

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.smart.smartbalibackpaker.core.data.source.local.entity.GuideMapsEntity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

object ShortestPath {
    private val INFINITE = 999999
    private var k = 0
    private val listPlace = ArrayList<GuideMapsEntity?>()
    private var nodes = ArrayList<Int?>()
    val choosenPath : LinkedHashMap<MutableList<Int>, Int> = LinkedHashMap()
    var route = ArrayList<Int>()
    var arr : Array<ArrayList<Int>> = arrayOf()
    var sumDistance : Array<Int> = arrayOf()
    val shortestPathRoute = ArrayList<Int>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDistances(listPlace: ArrayList<GuideMapsEntity>, node: HashMap<Any, Int?>){
        k = listPlace.size
        this.listPlace.clear()
        this.listPlace.addAll(listPlace)

        val sortedNode : MutableMap<Any, Int?> = TreeMap(node)

        nodes.clear()
        nodes = ArrayList(sortedNode.values)
        Log.d("hasillistplace2", listPlace.toString())
        Log.d("nodes", sortedNode.toString())
        Log.d("nodessize", nodes.size.toString())
        Log.d("k", k.toString())

            nodes.forEachIndexed { index, _ ->
                if (index == nodes.size - 1){
                    createTable()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createTable(){
        val table = Array(k) { IntArray(k) }
        var indexTable = 0
        arr = arrayOf()
        route.clear()

        // insert distance into table
        for(row in 0 until k){
            for (column in 0 until k){
                if(row == column){
                    table[row][column] = INFINITE
                } else {
                    table[row][column] = nodes[indexTable]!!
                }

                if(indexTable == nodes.size - 1){
                    indexTable = 0
                } else {
                    if(row != column){
                        indexTable++
                    }
                }
            }
        }
        // insert distance into table

        for(column in table){
            for(value in column){
                print(value)
                print(" ")
            }
            println("")
        }
        println("testhalo123")

        // dij > dik + dkj
        for(dk in 0 until k){
            for(row in 0 until k){
                for (column in 0 until k){
                    if(row != column){
                        val isFloyd = table[row][column] > table[row][dk] + table[dk][column]

                        if(isFloyd){
                            table[row][column] = table[row][dk] + table[dk][column]
                        } else {
                            table[row][column] = table[row][column]
                        }
                    }
                }
            }
        }
        // dij > dik + dkj

        // make route
        var row = 0
        var idx = 0
        val oneRowDistanceValue = ArrayList<Int>()
        val smallestIndexValue = ArrayList<Int>(arrayListOf(0))
        while (idx < k){
            listPlace.forEachIndexed { index, _ ->
                if(row > 0 && index in smallestIndexValue){
                    table[row][index] = INFINITE
                }
                oneRowDistanceValue.add(table[row][index])
                if (oneRowDistanceValue.size == k){
                    if (smallestIndexValue.size < k){
                        Log.d("row", "$row -> $oneRowDistanceValue")
                        val sortedTmpMinValue = ArrayList<Int>()
                        sortedTmpMinValue.addAll(oneRowDistanceValue)

                        val minValue = sortedTmpMinValue.sorted()[0]
                        val indexMinValue = oneRowDistanceValue.indexOf(minValue)

                        row = indexMinValue
                        smallestIndexValue.add(indexMinValue)
                        oneRowDistanceValue.clear()
                    }
                    idx++
                }
            }
        }

        shortestPathRoute.addAll(smallestIndexValue)
        // make route

        //      print table
        for(column in table){
            for(value in column){
                print(value)
                print(" ")
            }
            println("")
        }
        println("testhalo123456")
    }

    fun getShortestRoute(): List<GuideMapsEntity?> {
        Log.d("indexminvalue", shortestPathRoute.toString())
        // sort place by id
        val place = this.listPlace.associateBy { it?.id }
        val sortPlaceByShortest = shortestPathRoute.map { place[it + 1] }
        // set place number for arrange route
        sortPlaceByShortest.forEachIndexed { index, _ ->
            sortPlaceByShortest[index]?.placeNumber = index + 1
        }
        Log.d("hasillistplace3", sortPlaceByShortest.toString())
        return sortPlaceByShortest
    }
}