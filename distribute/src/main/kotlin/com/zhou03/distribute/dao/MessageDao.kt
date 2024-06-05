package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.Message
import com.zhou03.distribute.domain.Messages
import org.ktorm.dsl.*
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class MessageDao : BaseDao<Message, Messages>(Messages) {

    fun listByDateAsOwn(userId: Int, from: LocalDateTime, to: LocalDateTime) =
        findList { (it.type eq false) and (from less it.date) and (it.date less to) and ((it.from eq userId) or (it.to eq userId)) }

    fun listByDateAsOwn(ids: List<Int>, from: LocalDateTime, to: LocalDateTime) =
        if (ids.isNotEmpty()) findList { (it.type eq true) and (from less it.date) and (it.date less to) and (it.to inList ids) } else listOf()

    fun listByDateAsOwn(userId: Int, ids: List<Int>, from: LocalDateTime, to: LocalDateTime) =
        listByDateAsOwn(userId, from, to) + listByDateAsOwn(ids, from, to)

    fun getList(userId: Int) = findList { (it.from eq userId) }

    fun getByIdAsOwn(id: Int, own: Int) = findOne { (it.id eq id) and ((it.from eq own) or (it.to eq own)) }

    fun getById(id: Int) = findOne { it.id eq id }
}