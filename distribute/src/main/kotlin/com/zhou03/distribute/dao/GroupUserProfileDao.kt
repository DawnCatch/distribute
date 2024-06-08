package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.GroupUserProfile
import com.zhou03.distribute.domain.GroupUserProfiles
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.stereotype.Component

@Component
class GroupUserProfileDao : BaseDao<GroupUserProfile, GroupUserProfiles>(GroupUserProfiles) {

    fun getById(id: Int) = findOne { it.id eq id }

    fun listById(ids: List<Int>) = if (ids.isEmpty()) listOf() else findList { (it.id inList ids) }

    fun listByUserId(ids: List<Int>) = if (ids.isEmpty()) listOf() else findList { (it.userId inList ids) }

    fun listByGroupId(id: Int) = findList { (it.groupId eq id) }
}