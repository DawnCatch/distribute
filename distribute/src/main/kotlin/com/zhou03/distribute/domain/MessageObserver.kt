package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import java.time.LocalDateTime

interface MessageObserver : Entity<MessageObserver> {
    companion object : Entity.Factory<MessageObserver>()

    var id: Int

    var messageId: Int

    var userId: Int

    var date: LocalDateTime
}

object MessageObservers : Table<MessageObserver>("t_message_observer") {
    val id = int("id").primaryKey().bindTo { it.id }
    val messageId = int("message_id").bindTo { it.messageId }
    val userId = int("user_id").bindTo { it.userId }
    val date = datetime("date").bindTo { it.date }
}

val Database.messageObservers get() = this.sequenceOf(MessageObservers)