package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.GroupUserProfile
import com.zhou03.distribute.domain.GroupUserProfiles
import com.zhou03.distribute.domain.Profile
import com.zhou03.distribute.domain.Profiles
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.stereotype.Component

@Component
class GroupUserProfileDao : BaseDao<GroupUserProfile, GroupUserProfiles>(GroupUserProfiles) {

    fun getById(id: Int) = findOne { it.id eq id }

    fun listById(ids: List<Int>) = findList { (it.id inList ids) }
}