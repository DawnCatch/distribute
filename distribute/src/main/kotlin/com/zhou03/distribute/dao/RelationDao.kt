package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.Relation
import com.zhou03.distribute.domain.Relations
import com.zhou03.distribute.util.toLocalDateTime
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.notEq
import org.ktorm.dsl.or
import org.springframework.stereotype.Component

@Component
class RelationDao : BaseDao<Relation, Relations>(Relations) {

    fun getById(id: Int) = findOne { (it.id eq id) and (it.status eq false) and (it.date notEq 0L.toLocalDateTime()) }

    fun getIdAndId(arg1: Int, arg2: Int) =
        findOne { (it.userId eq arg1) and (it.targetId eq arg2) and (it.date notEq 0L.toLocalDateTime()) }

    fun listRelation(userId: Int) =
        findList { ((it.userId eq userId) or (it.targetId eq userId)) and (it.status eq true) }

    fun listApplication(userId: Int) =
        findList { (it.targetId eq userId) and (it.status eq false) and (it.date notEq 0L.toLocalDateTime()) }
}