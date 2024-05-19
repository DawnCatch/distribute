package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import java.time.LocalDateTime

interface Message : Entity<Message> {
    companion object : Entity.Factory<Message>()

    var id: Int

    var from: Int

    var to: Int

    var content: String

    var date: LocalDateTime
}

object Messages : Table<Message>("t_message") {
    val id = int("id").primaryKey().bindTo { it.id }
    val from = int("from").bindTo { it.from }
    val to = int("to").bindTo { it.to }
    val content = varchar("content").bindTo { it.content }
    val date = datetime("date").bindTo { it.date }
}

val Database.messages get() = this.sequenceOf(Messages)