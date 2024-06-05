package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import java.time.LocalDateTime

interface Group : Entity<Group> {
    companion object : Entity.Factory<Group>()

    var id: Int

    var title: String

    var creatorId: Int

    var createDate: LocalDateTime
}

object Groups : Table<Group>("t_group") {
    val id = int("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val creatorId = int("creator_id").bindTo { it.creatorId }
    val createDate = datetime("create_date").bindTo { it.createDate }
}

val Database.groups get() = this.sequenceOf(Groups)