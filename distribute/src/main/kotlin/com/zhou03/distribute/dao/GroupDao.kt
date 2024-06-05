package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.Group
import com.zhou03.distribute.domain.Groups
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.stereotype.Component

@Component
class GroupDao : BaseDao<Group, Groups>(Groups) {

    fun getById(id: Int) = findOne { it.id eq id }

    fun getByIdAsOwn(id: Int, userId: Int) = findOne { (it.id eq id) and (it.creatorId eq userId) }

    fun listById(ids: List<Int>) = findList { (it.id inList ids) }

    fun listAsOwn(userId: Int) = findList { it.creatorId eq userId }

    fun check(userId: Int) = count { it.creatorId eq userId } != 0
}