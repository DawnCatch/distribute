package com.zhou03.distribute.vo

data class RelationUnionVO(
    val follows: List<RelationVO>,
    val fans: List<RelationVO>,
    val groups: List<RelationVO>,
    val application: List<RelationVO>,
)
