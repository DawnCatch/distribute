package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.LocalDateTime

interface UserRelation : Entity<UserRelation> {
    companion object : Entity.Factory<UserRelation>()

    var id: Int

    var userId: Int

    var targetId: Int

    var status: Boolean

    var path: String

    var nickname: String

    var date: LocalDateTime
}

object UserRelations : Table<UserRelation>("t_user_relation") {
    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").bindTo { it.userId }
    val targetId = int("target_id").bindTo { it.targetId }
    val status = boolean("status").bindTo { it.status }
    val path = varchar("path").bindTo { it.path }
    val nickname = varchar("nickname").bindTo { it.nickname }
    val date = datetime("date").bindTo { it.date }
}

val Database.userRelations get() = this.sequenceOf(UserRelations)