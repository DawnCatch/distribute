package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Group
import com.zhou03.distribute.util.toMilliSecond

data class GroupVO(
    val id: Int,
    val title: String,
    val creatorId: Int,
    val createDate: Long,
) {
    companion object {

        fun from(group: Group) = GroupVO(group.id, group.title, group.creatorId, group.createDate.toMilliSecond())
    }
}
