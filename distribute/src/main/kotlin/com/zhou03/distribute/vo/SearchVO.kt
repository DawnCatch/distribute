package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Group
import com.zhou03.distribute.domain.User

data class SearchVO(
    val type: Boolean,
    val id: Int,
) {
    companion object {
        fun from(user: User) = SearchVO(false, user.id)

        fun from(group: Group) = SearchVO(true, group.id)
    }
}
