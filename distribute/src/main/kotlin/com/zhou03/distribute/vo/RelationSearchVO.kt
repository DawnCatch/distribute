package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Group
import com.zhou03.distribute.domain.Profile

data class RelationSearchVO(
    val type: Boolean,
    val id: Int,
    val title: String,
) {
    companion object {
        fun from(profile: Profile) = RelationSearchVO(false, profile.id, profile.nickname)

        fun from(group: Group) = RelationSearchVO(true, group.id, group.title)
    }
}