package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface Device : Entity<Device> {
    companion object : Entity.Factory<Device>()

    var id: Int

    var userId: Int

    var title: String

    var code: String

    var status: Boolean
}

object Devices : Table<Device>("t_device") {
    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").bindTo { it.userId }
    val title = varchar("title").bindTo { it.title }
    val code = varchar("code").bindTo { it.code }
    val status = boolean("status").bindTo { it.status }
}

val Database.devices get() = this.sequenceOf(Devices)