package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.GroupUserRelation

data class PendRelationVO(
    val id: Int,
    val userId: Int,
    val groupId: Int,
) {
    companion object {
        fun from(relation: GroupUserRelation) = PendRelationVO(relation.id, relation.userId, relation.targetId)
    }
}