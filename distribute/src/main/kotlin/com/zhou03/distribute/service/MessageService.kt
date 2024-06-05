package com.zhou03.distribute.service

import com.zhou03.distribute.dao.DeviceDao
import com.zhou03.distribute.dao.MessageDao
import com.zhou03.distribute.dao.MessageObserverDao
import com.zhou03.distribute.dao.RelationDao
import com.zhou03.distribute.domain.Message
import com.zhou03.distribute.domain.MessageObserver
import com.zhou03.distribute.dto.message.MessageHistoryDTO
import com.zhou03.distribute.dto.message.MessageReadDTO
import com.zhou03.distribute.dto.message.MessageSendDTO
import com.zhou03.distribute.util.ChatUtil
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.util.toJson
import com.zhou03.distribute.util.toLocalDateTime
import com.zhou03.distribute.vo.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.SQLIntegrityConstraintViolationException
import java.time.LocalDateTime

interface MessageService {

    fun history(messageHistoryDTO: MessageHistoryDTO, request: HttpServletRequest): Result<List<MessageVO>?>

    fun userSend(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?>

    fun userSend(key: String, messageSendDTO: MessageSendDTO): Result<Nothing?>

    fun groupSend(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?>

    fun groupSend(key: String, messageSendDTO: MessageSendDTO): Result<Nothing?>

    fun read(messageReadDTO: MessageReadDTO, request: HttpServletRequest): Result<Nothing?>
}

@Service
class MessageServiceImpl : MessageService {

    @Autowired
    lateinit var messageDao: MessageDao

    @Autowired
    lateinit var deviceDao: DeviceDao

    @Autowired
    lateinit var relationDao: RelationDao

    @Autowired
    lateinit var messageObserverDao: MessageObserverDao

    override fun history(messageHistoryDTO: MessageHistoryDTO, request: HttpServletRequest): Result<List<MessageVO>?> {
        val token = request.getToken()
        val from = messageHistoryDTO.from.toLocalDateTime()
        val to = messageHistoryDTO.to.toLocalDateTime()
        val relations = relationDao.listRelation(true, token.userId)
        val messageDomains = messageDao.listByDateAsOwn(token.userId, relations.map { it.id }, from, to)
        val ids = messageDomains.map { it.id }
        val messageObserversMap = messageObserverDao.listByMessageId(ids).groupBy { it.messageId }
        val messages = messageDomains.map {
            MessageVO.from(it)
        }
        messages.forEach { it ->
            val messageObservers = messageObserversMap[it.id]
            if (messageObservers.isNullOrEmpty()) {
                it.observers = listOf()
            } else {
                it.observers = messageObservers.map { it.userId }
            }
        }
        return success(messages)
    }

    override fun userSend(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?> {
        if (messageSendDTO.validate()) return error(
            "错误格式"
        )
        val token = request.getToken()
        if (messageSendDTO.to != -1 && messageSendDTO.to != token.userId && !relationDao.haveRelation(
                false, token.userId, messageSendDTO.to
            )
        ) return error("发生失败")
        val messageDomain = Message().apply {
            type = false
            from = token.userId
            to = messageSendDTO.to
            content = toJson(messageSendDTO.content)
            date = LocalDateTime.now()
        }
        messageDao.add(messageDomain)
        val message = MessageVO.from(messageDomain)
        ChatUtil.sendMessage(message)
        return success(null, "发送成功")
    }

    override fun userSend(key: String, messageSendDTO: MessageSendDTO): Result<Nothing?> {
        if (messageSendDTO.validate()) return error(
            "错误格式"
        )
        val device = deviceDao.check(key) ?: return error("验证失败")
        if (messageSendDTO.to != -1 && messageSendDTO.to != device.userId && !relationDao.haveRelation(
                false, device.userId, messageSendDTO.to
            )
        ) return error("发生失败")
        val messageDomain = Message().apply {
            type = false
            from = device.userId
            to = if (messageSendDTO.to == -1) device.userId else messageSendDTO.to
            content = toJson(messageSendDTO.content)
            date = LocalDateTime.now()
        }
        messageDao.add(messageDomain)
        val message = MessageVO.from(messageDomain)
        ChatUtil.sendMessage(message)
        return success(message = "发送成功")
    }

    override fun groupSend(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?> {
        if (messageSendDTO.validate()) return error(
            "错误格式"
        )
        val token = request.getToken()
        val ids = relationDao.listByGroupId(messageSendDTO.to).map { it.userId }
        if (ids.isEmpty() || token.userId !in ids) return error(
            "发送失败"
        )
        val messageDomain = Message().apply {
            type = true
            from = token.userId
            to = messageSendDTO.to
            content = toJson(messageSendDTO.content)
            date = LocalDateTime.now()
        }
        messageDao.add(messageDomain)
        val message = MessageVO.from(messageDomain)
        ChatUtil.sendMessage(message, ids)
        return success(message = "发送成功")
    }

    override fun groupSend(key: String, messageSendDTO: MessageSendDTO): Result<Nothing?> {
        if (messageSendDTO.validate()) return error(
            "错误格式"
        )
        val device = deviceDao.check(key) ?: return error("验证失败")
        val ids = relationDao.listByGroupId(messageSendDTO.to).map { it.userId }
        if (ids.isEmpty() || device.userId !in ids) return error(
            "发送失败"
        )
        val messageDomain = Message().apply {
            type = false
            from = device.userId
            to = messageSendDTO.to
            content = toJson(messageSendDTO.content)
            date = LocalDateTime.now()
        }
        messageDao.add(messageDomain)
        val message = MessageVO.from(messageDomain)
        ChatUtil.sendMessage(message, ids)
        return success(message = "发送成功")
    }

    override fun read(messageReadDTO: MessageReadDTO, request: HttpServletRequest): Result<Nothing?> {
        val token = request.getToken()
        val message = messageDao.getById(messageReadDTO.id) ?: return error("目标不存在")
        val messageObserver = MessageObserver().apply {
            this.messageId = message.id
            this.userId = token.userId
            this.date = LocalDateTime.now()
        }
        try {
            if (message.type) {
                val relations = relationDao.listByGroupId(message.to)
                val relationIds = relations.map { it.userId }
                if (token.userId !in relationIds) return error("权限错误")
                messageObserverDao.add(messageObserver)
                ChatUtil.sendMessage(
                    MessageVO(
                        id = message.id, from = token.userId, to = message.to, content = Content(
                            type = "OBSERVER", value = ""
                        )
                    ), relationIds
                )
            } else {
                if (token.userId !in listOf(message.from, message.to)) return error("权限错误")
                messageObserverDao.add(messageObserver)
                ChatUtil.sendMessage(
                    MessageVO(
                        id = message.id,
                        from = token.userId,
                        to = message.getOtherParty(token.userId),
                        content = Content(
                            type = "OBSERVER", value = ""
                        )
                    )
                )
            }
        } catch (_: SQLIntegrityConstraintViolationException) {
        }
        return success()
    }
}