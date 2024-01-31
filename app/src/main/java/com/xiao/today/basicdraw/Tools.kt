package com.xiao.today.basicdraw

import kotlin.random.Random

fun randomString(size: Int, additional: (() -> String)? = null): String{
    val lowercase = ('a' .. 'z').toList()
    val stringBuilder = StringBuilder()
    for (i in 0 until size) {
        stringBuilder.append(lowercase[Random.nextInt(1, lowercase.size)])
    }
    stringBuilder.append(additional?.invoke())
    return stringBuilder.toString()
}

fun randomIntList(from: Int, until: Int): List<Int>{
    val dataList = mutableListOf<Int>()
    val nextInt = Random.nextInt(from, until)
    for (i in 1..nextInt) {
        dataList.add(i)
    }
    return dataList
}
