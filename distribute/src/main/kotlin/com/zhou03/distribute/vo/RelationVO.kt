package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Group
import com.zhou03.distribute.domain.GroupUserRelation
import com.zhou03.distribute.domain.Profile
import com.zhou03.distribute.domain.UserRelation

data class RelationVO(
    val id: Int,
    val type: Boolean,
    val targetId: Int,
    val title: String,
    val nickname: String,
    val role: String,
    val path: String,
) {
    companion object {
        fun from(relation: GroupUserRelation, group: Group) =
            RelationVO(relation.id, true, group.id, group.title, relation.nickname, relation.role, relation.path)

        fun from(relation: UserRelation?, profile: Profile) = RelationVO(
            relation?.id ?: 0, false, profile.id, profile.nickname, relation?.nickname ?: "", "", relation?.path ?: "/"
        )
    }
}