package com.zhou03.distribute.vo

data class ApplicationVO(
    val relationId: Int,
    val type: Boolean,
    val userId: Int,
    val targetId: Int,
    val nickname: String,
    val title: String,
    val date: Long,
)