package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface Profile : Entity<Profile> {
    companion object : Entity.Factory<Profile>()

    var id: Int

    var nickname: String

    var avatarUrl: String

    var auth: Int

    fun changeAuth(auth: Int) = apply {
        if (this.auth == 0) return@apply
        this.auth = auth
    }
}

object Profiles : Table<Profile>("t_profile") {
    val id = int("id").primaryKey().bindTo { it.id }
    val nickname = varchar("nickname").bindTo { it.nickname }
    val avatarUrl = varchar("avatar_url").bindTo { it.avatarUrl }
    val auth = int("auth").bindTo { it.auth }
}

object Auth {
    const val GUEST = 0
    const val DEVICE = 1
    const val USER = 2
    const val ADMIN = 3
}

val Database.profiles get() = this.sequenceOf(Profiles)