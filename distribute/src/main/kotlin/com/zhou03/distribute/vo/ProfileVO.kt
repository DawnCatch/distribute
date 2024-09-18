package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Profile

data class ProfileVO(
    val userId: Int,
    val nickname: String,
    val avatarUrl: String,
) {
    companion object {
        fun from(profile: Profile) = ProfileVO(profile.id, profile.nickname, profile.avatarUrl)
    }
}