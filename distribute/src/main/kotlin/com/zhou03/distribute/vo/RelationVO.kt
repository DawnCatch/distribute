package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.UserRelation

data class RelationVO(
    val type: Boolean,
    val id: Int,
    val title: String,
    val path: String,
)