package com.zhou03.distribute.dto.user

data class RequestGroupBatchInvite(
    val groupId: Int,
    val userIds: List<Int>,
)
