package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Profile

data class RelationSearchVO(
    val userId: Int,
    val nickname: String,
) {
    companion object {
        fun from(profile: Profile) = RelationSearchVO(profile.id, profile.nickname)
    }
}