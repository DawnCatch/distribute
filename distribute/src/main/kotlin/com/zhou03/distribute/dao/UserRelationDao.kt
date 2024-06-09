package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.UserRelation
import com.zhou03.distribute.domain.UserRelations
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

@Component
class UserRelationDao : BaseDao<UserRelation, UserRelations>(UserRelations) {

    fun isRelation(userId: Int, targetId: Int) =
        count { (it.status eq true) and (it.userId eq userId) and (it.targetId eq targetId) } != 0

    fun listFollowAsOwn(userId: Int) = findList { (it.status eq true) and (it.userId eq userId) }

    fun listFanAsOwn(userId: Int) = findList { (it.status eq true) and (it.targetId eq userId) }

    fun getByUserIdAndTargetId(userId: Int, targetId: Int) =
        findOne { (it.userId eq userId) and (it.targetId eq targetId) }

    override fun updateUnion(entity: UserRelation): Int {
        val oldEntity = getByUserIdAndTargetId(entity.userId, entity.targetId) ?: return 0
        entity.apply {
            this.id = oldEntity.id
        }
        update(entity)
        return 1
    }
}