package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.LocalDateTime

interface Group : Entity<Group> {
    companion object : Entity.Factory<Group>()

    var id: Int

    var title: String

    var createDate: LocalDateTime

    var visible: Boolean

    var status: Boolean
}

object Groups : Table<Group>("t_group") {
    val id = int("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val createDate = datetime("create_date").bindTo { it.createDate }
    val visible = boolean("visible").bindTo { it.visible }
    val status = boolean("status").bindTo { it.status }
}

val Database.groups get() = this.sequenceOf(Groups)