package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.MessageObserver
import com.zhou03.distribute.domain.MessageObservers
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.springframework.stereotype.Component

@Component
class MessageObserverDao : BaseDao<MessageObserver, MessageObservers>(MessageObservers) {

    fun getById(id: Int) = findOne { it.id eq id }

    fun listByMessageId(ids: List<Int>) = if (ids.isNotEmpty()) findList { (it.messageId inList ids) } else listOf()
}