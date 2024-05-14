package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface Profile : Entity<Profile> {
    companion object : Entity.Factory<Profile>()

    var id: Int
    var nickname: String
}

object Profiles : Table<Profile>("t_profile") {
    val id = int("id").primaryKey().bindTo { it.id }
    val nickname = varchar("nickname").bindTo { it.nickname }
}

val Database.profiles get() = this.sequenceOf(Profiles)