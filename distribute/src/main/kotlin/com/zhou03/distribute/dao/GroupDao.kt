package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.Group
import com.zhou03.distribute.domain.Groups
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

@Component
class GroupDao : BaseDao<Group, Groups>(Groups) {

    fun getById(id: Int) = findOne { (it.id eq id) and (it.status eq true) }
}