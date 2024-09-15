package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Group
import com.zhou03.distribute.util.toMilliSecond

data class GroupVO(
    val groupId: Int,
    val title: String,
    val visible: Boolean,
    val createDate: Long,
) {
    companion object {

        fun from(group: Group) = GroupVO(
            group.id, group.title, group.visible, group.createDate.toMilliSecond()
        )
    }
}