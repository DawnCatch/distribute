package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.GroupUserProfile

data class GroupUserProfileVO(
    val userId: Int,
    val nickname: String,
) {
    companion object {

        fun from(groupUserProfile: GroupUserProfile) =
            GroupUserProfileVO(groupUserProfile.userId, groupUserProfile.nickname)

        fun from(groupUserProfiles: List<GroupUserProfile>) = groupUserProfiles.map { from(it) }
    }
}