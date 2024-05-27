package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.LocalDateTime

interface Relation : Entity<Relation> {
    companion object : Entity.Factory<Relation>()

    var id: Int

    var userId: Int

    var tagetId: Int

    var status: Boolean

    var date: LocalDateTime

    var path: String
}

object Relations : Table<Relation>("t_relation") {
    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").bindTo { it.userId }
    val targetId = int("target_id").bindTo { it.tagetId }
    val status = boolean("status").bindTo { it.status }
    val date = datetime("date").bindTo { it.date }
    val path = varchar("path").bindTo { it.path }
}

val Database.relations get() = this.sequenceOf(Relations)