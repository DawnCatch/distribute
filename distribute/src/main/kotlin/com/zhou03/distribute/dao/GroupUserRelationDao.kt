package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.GroupRole
import com.zhou03.distribute.domain.GroupUserRelation
import com.zhou03.distribute.domain.GroupUserRelations
import com.zhou03.distribute.util.toLocalDateTime
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.dsl.notEq
import org.ktorm.entity.mapColumnsNotNull
import org.ktorm.entity.toList
import org.springframework.stereotype.Component

@Component
class GroupUserRelationDao : BaseDao<GroupUserRelation, GroupUserRelations>(GroupUserRelations) {

    fun getById(id: Int) = findOne { it.id eq id }

    fun getByTargetIdAsOwn(targetId: Int, userId: Int) = findOne { (it.userId eq userId) and (it.targetId eq targetId) }

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

    fun listUserIdByGroupAsManager(groupId: Int) = findEntitySequence {
        (it.targetId eq groupId) and (it.role inList listOf(
            GroupRole.MANAGER, GroupRole.MASTER
        ))
    }.mapColumnsNotNull { it.userId }

    fun listByJoinAsOwn(userId: Int) =
        findEntitySequence { (it.status eq true) and (it.userId eq userId) }.mapColumnsNotNull { it.targetId }

    fun listByPendingAsOwn(userId: Int) =
        findEntitySequence { (it.status eq false) and (it.date notEq 0L.toLocalDateTime()) and (it.userId eq userId) }.mapColumnsNotNull { it.id }

    fun listByMeAsAManager(userId: Int) = findEntitySequence {
        (it.status eq true) and (it.userId eq userId) and (it.role inList listOf(
            GroupRole.MANAGER, GroupRole.MASTER
        ))
    }.mapColumnsNotNull { it.targetId }

    fun listMemberIdByTargetIdSequence(id: Int) = findEntitySequence { (it.status eq true) and (it.targetId eq id) }

    fun listMemberByTargetId(id: Int) = listMemberIdByTargetIdSequence(id).toList()

    fun listMemberIdByTargetId(id: Int) = listMemberIdByTargetIdSequence(id).mapColumnsNotNull { it.userId }

    fun countMemberByTargetId(id: Int) = count { (it.status eq true) and (it.targetId eq id) }

    fun listPending(groupIds: List<Int>) =
        if (groupIds.isNotEmpty()) findEntitySequence { (it.status eq false) and (it.date notEq 0L.toLocalDateTime()) and (it.targetId inList groupIds) }.mapColumnsNotNull { it.id }
        else listOf()

    fun getApplicationAsOwn(userId: Int, targetId: Int) =
        findOne { (it.userId eq userId) and (it.targetId eq targetId) and (it.date notEq 0L.toLocalDateTime()) }

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