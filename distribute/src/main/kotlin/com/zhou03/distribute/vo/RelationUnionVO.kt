package com.zhou03.distribute.vo

data class RelationUnionVO(
    val follows: List<RelationVO>,
    val fans: List<Int>,
    val groups: List<RelationVO>,
    val applications: List<RelationVO>,
)
