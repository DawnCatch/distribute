package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.Message
import com.zhou03.distribute.domain.Messages
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.less
import org.ktorm.dsl.or
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class MessageDao : BaseDao<Message, Messages>(Messages) {

    fun getListOfDate(userId: Int, from: LocalDateTime, to: LocalDateTime) =
        findList { (from less it.date) and (it.date less to) and (it.from eq userId) or (it.to eq userId) }
}