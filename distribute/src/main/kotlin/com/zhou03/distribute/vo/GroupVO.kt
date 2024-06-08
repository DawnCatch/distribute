package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Group
import com.zhou03.distribute.domain.GroupUserProfile
import com.zhou03.distribute.util.toMilliSecond

data class GroupVO(
    val id: Int,
    val title: String,
    val creatorId: Int,
    val createDate: Long,
    val members: List<GroupUserProfileVO>,
) {
    companion object {

        fun from(group: Group) = GroupVO(
            group.id, group.title, group.creatorId, group.createDate.toMilliSecond(), listOf()
        )

        fun from(group: Group, members: List<GroupUserProfile>) = GroupVO(
            group.id, group.title, group.creatorId, group.createDate.toMilliSecond(), GroupUserProfileVO.from(members)
        )

        fun from(group: Group, members: List<GroupUserProfileVO>) =
            GroupVO(group.id, group.title, group.creatorId, group.createDate.toMilliSecond(), members)
    }
}