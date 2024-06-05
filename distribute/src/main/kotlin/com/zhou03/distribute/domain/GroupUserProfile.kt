package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface GroupUserProfile : Entity<GroupUserProfile> {
    companion object : Entity.Factory<GroupUserProfile>()

    var id: Int

    var groupId: Int

    var userId: Int

    var nickname: String
}

object GroupUserProfiles : Table<GroupUserProfile>("t_group__user_profile") {
    val id = int("id").primaryKey().bindTo { it.id }
    val groupId = int("group_id").bindTo { it.groupId }
    val userId = int("user_id").bindTo { it.userId }
    val nickname = varchar("nickname").bindTo { it.nickname }
}

val Database.groupUserProfiles get() = this.sequenceOf(GroupUserProfiles)