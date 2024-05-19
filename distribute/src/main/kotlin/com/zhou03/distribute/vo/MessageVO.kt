package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Message
import com.zhou03.distribute.util.fromJson
import com.zhou03.distribute.util.toJson
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import java.time.LocalDateTime

data class MessageVO(
    val id: Int = 0,
    val from: Int,
    val to: Int,
    val content: List<Content>,
    val date: LocalDateTime? = LocalDateTime.now(),
) {
    companion object {
        fun from(message: WebSocketMessage<*>) = fromJson<MessageVO>(message.payload as String)

        fun from(message: Message) =
            MessageVO(message.id, message.from, message.to, fromJson(message.content), message.date)
    }

    fun to() = TextMessage(toJson(this))

    fun toDomain(): Message = Message().apply {
        this.from = this@MessageVO.from
        this.to = this@MessageVO.to
        this.content = toJson(this@MessageVO.content)
        if (this@MessageVO.date == null) {
            this.date = LocalDateTime.now()
        } else {
            this.date = this@MessageVO.date
        }
    }
}

data class Content(
    val type: String,
    val value: String,
)