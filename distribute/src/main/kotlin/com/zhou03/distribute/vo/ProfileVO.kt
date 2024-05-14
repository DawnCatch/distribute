package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Profile

data class ProfileVO(
    val nickname: String,
) {
    companion object {
        fun from(profile: Profile) = ProfileVO(profile.nickname)
    }
}