package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.GroupRole
import com.zhou03.distribute.domain.GroupUserRelation
import com.zhou03.distribute.domain.GroupUserRelations
import com.zhou03.distribute.util.toLocalDateTime
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.dsl.notEq
import org.springframework.stereotype.Component

@Component
class GroupUserRelationDao : BaseDao<GroupUserRelation, GroupUserRelations>(GroupUserRelations) {

    fun getById(id: Int) = findOne { it.id eq id }

    fun isRelation(userId: Int, targetId: Int) =
        count { (it.status eq true) and (it.userId eq userId) and (it.targetId eq targetId) } != 0

    fun isMember(userId: Int, targetId: Int) = count {
        (it.status eq true) and (it.userId eq userId) and (it.targetId eq targetId) and (it.role inList listOf(
            GroupRole.MEMBER, GroupRole.MANAGER, GroupRole.MASTER
        ))
    } != 0

    fun isManager(userId: Int, targetId: Int) = count {
        (it.status eq true) and (it.userId eq userId) and (it.targetId eq targetId) and (it.role inList listOf(
            GroupRole.MANAGER, GroupRole.MASTER
        ))
    } != 0

    fun isMaster(userId: Int, targetId: Int) = count {
        (it.status eq true) and (it.userId eq userId) and (it.targetId eq targetId) and (it.role eq GroupRole.MASTER)
    } != 0

    fun listByJoinAsOwn(userId: Int) = findList { (it.status eq true) and (it.userId eq userId) }

    fun listByPendingAsOwn(userId: Int) =
        findList { (it.status eq false) and (it.date notEq 0L.toLocalDateTime()) and (it.userId eq userId) }

    fun listByMeAsAManager(userId: Int) = findList {
        (it.status eq true) and (it.userId eq userId) and (it.role inList listOf(
            GroupRole.MANAGER, GroupRole.MASTER
        ))
    }

    fun listByTargetId(id: Int) = findList { (it.status eq true) and (it.targetId eq id) }

    fun listPending(groupIds: List<Int>) =
        if (groupIds.isNotEmpty()) findList { (it.status eq false) and (it.date notEq 0L.toLocalDateTime()) and (it.targetId inList groupIds) }
        else listOf()

    fun getApplicationAsOwn(userId: Int,targetId: Int) = findOne { (it.userId eq userId) and (it.targetId eq targetId) and (it.date notEq 0L.toLocalDateTime()) }

    fun getByUserIdAndTargetId(userId: Int, targetId: Int) =
        findOne { (it.userId eq userId) and (it.targetId eq targetId) }

    override fun updateUnion(entity: GroupUserRelation): Int {
        val oldEntity = getByUserIdAndTargetId(entity.userId, entity.targetId) ?: return 0
        entity.apply {
            this.id = oldEntity.id
        }
        update(entity)
        return 1
    }
}