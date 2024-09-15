package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.Profile
import com.zhou03.distribute.domain.Profiles
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.stereotype.Component

@Component
class ProfileDao : BaseDao<Profile, Profiles>(Profiles) {

    fun getById(id: Int) = findOne { it.id eq id }

    fun listById(ids: List<Int>) = if (ids.isNotEmpty()) findList { (it.id inList ids) } else listOf()
}