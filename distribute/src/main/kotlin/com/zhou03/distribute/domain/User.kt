package com.zhou03.distribute.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface User : Entity<User> {
    companion object : Entity.Factory<User>()

    var id: Int

    var username: String

    var password: String

    var email: String

    var auth: Int

    fun changeAuth(auth: Int) = apply {
        if (this.auth == 0) return@apply
        this.auth = auth
    }
}

object Users : Table<User>("t_user") {
    val id = int("id").primaryKey().bindTo { it.id }
    val username = varchar("username").bindTo { it.username }
    val password = varchar("password").bindTo { it.password }
    val email = varchar("email").bindTo { it.email }
    val auth = int("auth").bindTo { it.auth }
}

object Auth {
    const val GUEST = 0
    const val DEVICE = 1
    const val USER = 2
    const val ADMIN = 3
}

val Database.users get() = this.sequenceOf(Users)