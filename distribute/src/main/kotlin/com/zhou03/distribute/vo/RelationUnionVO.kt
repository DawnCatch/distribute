package com.zhou03.distribute.vo

data class RelationUnionVO(
    val follows: List<Int>,
    val fans: List<Int>,
    val groups: List<Int>,
    val applications: List<Int>,
    val pends: List<Int>,
)
