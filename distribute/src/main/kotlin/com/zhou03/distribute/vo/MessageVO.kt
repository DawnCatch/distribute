package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Message
import com.zhou03.distribute.util.fromJson
import com.zhou03.distribute.util.toJson
import com.zhou03.distribute.util.toLocalDateTime
import com.zhou03.distribute.util.toMilliSecond
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import java.time.LocalDateTime

data class MessageVO(
    val id: Int = 0,
    val type: Boolean,
    var from: Int = 0,
    val to: Int,
    val content: Content,
    val date: Long? = LocalDateTime.now().toMilliSecond(),
    var observers: List<Int> = listOf(),
) {
    companion object {
        fun from(message: WebSocketMessage<*>) = fromJson<MessageVO>(message.payload as String)

        fun from(message: Message) = MessageVO(
            message.id, message.type, message.from, message.to, fromJson(message.content), message.date.toMilliSecond()
        )

        fun create(type: Boolean, from: Int, to: Int, content: Content) =
            MessageVO(type = type, from = from, to = to, content = content)
    }

    fun to() = TextMessage(toJson(this))

    fun toDomain(): Message = Message().apply {
        this.from = this@MessageVO.from
        this.to = this@MessageVO.to
        this.content = toJson(this@MessageVO.content)
        if (this@MessageVO.date == null) {
            this.date = LocalDateTime.now()
        } else {
            this.date = this@MessageVO.date.toLocalDateTime()
        }
    }
}

data class Content(
    val type: String,
    val value: String,
)

class ContentType {
    companion object {
        const val TEXT = "TEXT"


        const val GROUP_CHANGE = "GROUP_CHANGE"
        const val GROUP_DELETE = "GROUP_DELETE"
        const val GROUP_MEMBER_CHANGE = "GROUP_MEMBER_CHANGE"
    }
}