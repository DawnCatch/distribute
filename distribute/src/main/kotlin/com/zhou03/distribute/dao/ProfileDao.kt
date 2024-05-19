package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.Profile
import com.zhou03.distribute.domain.Profiles
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

@Component
class ProfileDao : BaseDao<Profile, Profiles>(Profiles) {

    fun getById(id: Int) = findOne { it.id eq id }
}