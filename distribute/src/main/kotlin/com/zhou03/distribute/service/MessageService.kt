package com.zhou03.distribute.service

import com.zhou03.distribute.dao.DeviceDao
import com.zhou03.distribute.dao.MessageDao
import com.zhou03.distribute.dao.MessageObserverDao
import com.zhou03.distribute.dao.RelationDao
import com.zhou03.distribute.domain.Message
import com.zhou03.distribute.domain.MessageObserver
import com.zhou03.distribute.dto.MessageHistoryDTO
import com.zhou03.distribute.dto.MessageReadDTO
import com.zhou03.distribute.dto.MessageSendDTO
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

    fun send(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?>

    fun send(key: String, messageSendDTO: MessageSendDTO): Result<Nothing?>

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
        val messageDomains = messageDao.getListOfDate(token.userId, from, to)
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

    override fun send(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?> {
        if (messageSendDTO.to == 0 || messageSendDTO.content.type == "" || messageSendDTO.content.value == "") return error(
            "错误格式"
        )
        val token = request.getToken()
        val ids = relationDao.listRelation(token.userId).map { it.tagetId }
        if (ids.isEmpty() || messageSendDTO.to !in ids && messageSendDTO.to != -1 && messageSendDTO.to != token.userId) return error(
            "发送失败"
        )
        val messageDomain = Message().apply {
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

    override fun send(key: String, messageSendDTO: MessageSendDTO): Result<Nothing?> {
        if (messageSendDTO.to == 0 || messageSendDTO.content.type == "" || messageSendDTO.content.value == "") return error(
            "错误格式"
        )
        val device = deviceDao.check(key) ?: return error("验证失败")
        val ids = relationDao.listRelation(device.userId).map { it.tagetId }
        if (ids.isEmpty() || messageSendDTO.to !in ids && messageSendDTO.to != -1 && messageSendDTO.to != device.userId) return error(
            "发送失败"
        )
        val messageDomain = Message().apply {
            from = device.userId
            to = if (messageSendDTO.to == -1) device.userId else messageSendDTO.to
            content = toJson(messageSendDTO.content)
            date = LocalDateTime.now()
        }
        val message = MessageVO.from(messageDomain)
        ChatUtil.sendMessage(message)
        return success(null, "发送成功")
    }

    override fun read(messageReadDTO: MessageReadDTO, request: HttpServletRequest): Result<Nothing?> {
        val token = request.getToken()
        val message = messageDao.getByIdAsOwn(messageReadDTO.id, token.userId) ?: return error("权限错误")
        val messageObserver = MessageObserver().apply {
            this.messageId = message.id
            this.userId = token.userId
            this.date = LocalDateTime.now()
        }
        try {
            messageObserverDao.add(messageObserver)
            ChatUtil.sendMessage(
                MessageVO(
                    id = message.id, from = token.userId, to = message.getOtherParty(token.userId), content = Content(
                        type = "OBSERVER", value = ""
                    )
                )
            )
        } catch (_: SQLIntegrityConstraintViolationException) {
        }
        return success()
    }
}