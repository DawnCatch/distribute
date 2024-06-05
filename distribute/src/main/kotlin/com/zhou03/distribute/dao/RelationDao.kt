package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.Relation
import com.zhou03.distribute.domain.Relations
import com.zhou03.distribute.util.toLocalDateTime
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.dsl.notEq
import org.springframework.stereotype.Component

@Component
class RelationDao : BaseDao<Relation, Relations>(Relations) {

    fun getApplicationById(id: Int) =
        findOne { (it.id eq id) and (it.status eq false) and (it.date notEq 0L.toLocalDateTime()) }

    fun getIdAndId(type: Boolean, arg1: Int, arg2: Int) =
        findOne { (it.type eq type) and (it.userId eq arg1) and (it.targetId eq arg2) and (it.date notEq 0L.toLocalDateTime()) }

    fun listRelation(type: Boolean, userId: Int) =
        findList { (it.type eq type) and (it.userId eq userId) and (it.status eq true) }

    fun listUserApplication(userId: Int) =
        findList { (it.type eq false) and (it.targetId eq userId) and (it.status eq false) and (it.date notEq 0L.toLocalDateTime()) }

    fun listGroupApplication(ids: List<Int>) =
        if (ids.isNotEmpty()) findList { (it.type eq true) and (it.targetId inList ids) and (it.status eq false) and (it.date notEq 0L.toLocalDateTime()) } else listOf()

    fun haveRelation(type: Boolean, userId: Int, targetId: Int) =
        count { (it.type eq type) and (it.userId eq userId) and (it.targetId eq targetId) and (it.status eq true) } != 0

    fun listByGroupId(groupId: Int) = listRelation(true, groupId)
}