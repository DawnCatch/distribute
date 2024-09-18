package com.zhou03.distribute.service

import com.zhou03.distribute.dao.*
import com.zhou03.distribute.domain.Message
import com.zhou03.distribute.domain.MessageObserver
import com.zhou03.distribute.dto.message.MessageFileSendDTO
import com.zhou03.distribute.dto.message.MessageHistoryDTO
import com.zhou03.distribute.dto.message.MessageReadDTO
import com.zhou03.distribute.dto.message.MessageSendDTO
import com.zhou03.distribute.util.*
import com.zhou03.distribute.vo.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.sql.SQLIntegrityConstraintViolationException
import java.time.LocalDateTime

interface MessageService {

    fun history(messageHistoryDTO: MessageHistoryDTO, request: HttpServletRequest): Result<List<MessageVO>?>

    fun userSend(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?>

    fun userSend(key: String, messageSendDTO: MessageSendDTO): Result<Nothing?>

    fun userFileSend(messageFileSendDTO: MessageFileSendDTO, request: HttpServletRequest): Result<Nothing?>

    fun groupSend(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?>

    fun groupSend(key: String, messageSendDTO: MessageSendDTO): Result<Nothing?>

    fun groupFileSend(messageFileSendDTO: MessageFileSendDTO, request: HttpServletRequest): Result<Nothing?>

    fun read(messageReadDTO: MessageReadDTO, request: HttpServletRequest): Result<Nothing?>
}

@Service
class MessageServiceImpl : MessageService {

    @Autowired
    lateinit var messageDao: MessageDao

    @Autowired
    lateinit var deviceDao: DeviceDao

    @Autowired
    lateinit var userRelationDao: UserRelationDao

    @Autowired
    lateinit var groupUserRelationDao: GroupUserRelationDao

    @Autowired
    lateinit var messageObserverDao: MessageObserverDao

    @Value("\${file-save-path}")
    lateinit var fileSavePath: String

    override fun history(messageHistoryDTO: MessageHistoryDTO, request: HttpServletRequest): Result<List<MessageVO>?> {
        val token = request.getToken()
        fun toLocalDateTime(str: String): LocalDateTime {
            if (str == "") return 0L.toLocalDateTime()
            return try {
                str.toLong().toLocalDateTime()
            } catch (e: Exception) {
                str.toLocalDateTime()
            }
        }

        val from = toLocalDateTime(messageHistoryDTO.from)
        var to = toLocalDateTime(messageHistoryDTO.to)
        if (to <= from) to = LocalDateTime.now()
        val groupIds = groupUserRelationDao.listByJoinAsOwn(token.userId)
        val messageDomains = messageDao.listByDateAsOwn(token.userId, groupIds, from, to)
        val ownMessageIds = messageDomains.filter { it.from == token.userId }.map { it.id }
        val otherMessageIds = messageDomains.filter { it.from != token.userId }.map { it.id }
        val ownMessageObserversMap = messageObserverDao.listByMessageId(ownMessageIds).groupBy { it.messageId }
        val otherMessageObserversMap = messageObserverDao.listByMessageId(otherMessageIds).groupBy { it.messageId }
        val messages = messageDomains.map {
            MessageVO.from(it)
        }
        messages.forEach { it ->
            val ownMessageObservers = ownMessageObserversMap[it.id]
            val otherMessageObservers = otherMessageObserversMap[it.id]
            if (ownMessageObservers.isNullOrEmpty() && otherMessageObservers.isNullOrEmpty()) {
                it.observers = listOf()
            } else if (!ownMessageObservers.isNullOrEmpty()) {
                it.observers = ownMessageObservers.map { it.userId }
            } else if (!otherMessageObservers.isNullOrEmpty()) {
                it.observers = otherMessageObservers.map { it.userId }
            }
        }
        return success(messages)
    }

    override fun userSend(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?> {
        if (messageSendDTO.validate()) return error(
            "错误格式"
        )
        val token = request.getToken()
        if (messageSendDTO.to != -1 && messageSendDTO.to != token.userId && !userRelationDao.isRelation(
                token.userId, messageSendDTO.to
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
        if (messageSendDTO.to != -1 && messageSendDTO.to != device.userId && !userRelationDao.isRelation(
                device.userId, messageSendDTO.to
            )
        ) return error("发送失败")
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

    override fun userFileSend(messageFileSendDTO: MessageFileSendDTO, request: HttpServletRequest): Result<Nothing?> {
        if (messageFileSendDTO.validate()) return error("错误格式")
        val token = request.getToken()
        if (messageFileSendDTO.to != -1 && messageFileSendDTO.to != token.userId && !userRelationDao.isRelation(
                token.userId, messageFileSendDTO.to
            )
        ) return error("发生失败")
        val fileNames = upload(fileSavePath, messageFileSendDTO.files)
        if (fileNames.isEmpty()) return error("发送失败")
        val content = Content("FILE", fileNames.joinToString(","))
        val messageDomain = Message().apply {
            type = false
            from = token.userId
            to = messageFileSendDTO.to
            this.content = toJson(content)
            date = LocalDateTime.now()
        }
        messageDao.add(messageDomain)
        val message = MessageVO.from(messageDomain)
        ChatUtil.sendMessage(message)
        return success(null, "发送成功")
    }

    override fun groupSend(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?> {
        if (messageSendDTO.validate()) return error(
            "错误格式"
        )
        val token = request.getToken()
        val ids = groupUserRelationDao.listMemberIdByTargetId(messageSendDTO.to)
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
        val ids = groupUserRelationDao.listMemberIdByTargetId(messageSendDTO.to)
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

    override fun groupFileSend(messageFileSendDTO: MessageFileSendDTO, request: HttpServletRequest): Result<Nothing?> {
        if (messageFileSendDTO.validate()) return error("错误格式")
        val token = request.getToken()
        val ids = groupUserRelationDao.listMemberIdByTargetId(messageFileSendDTO.to)
        if (ids.isEmpty() || token.userId !in ids) return error(
            "发送失败"
        )
        val fileNames = upload(fileSavePath, messageFileSendDTO.files)
        if (fileNames.isEmpty()) return error("发送失败")
        val content = Content("FILE", fileNames.joinToString(","))
        val messageDomain = Message().apply {
            type = true
            from = token.userId
            to = messageFileSendDTO.to
            this.content = toJson(content)
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
                if (!groupUserRelationDao.isMember(token.userId, message.to)) return error("权限错误")
                messageObserverDao.addOrUpdate(messageObserver)
                ChatUtil.sendMessage(
                    MessageVO(
                        message.id, true, token.userId, message.to, Content(
                            ContentType.OBSERVER, ""
                        )
                    ), token.userId
                )

                if (!groupUserRelationDao.isMember(token.userId, message.to)) return error("权限错误")
                messageObserverDao.addOrUpdate(messageObserver)
                ChatUtil.sendMessage(
                    MessageVO(
                        message.id, true, token.userId, message.to, Content(
                            ContentType.OBSERVER, ""
                        )
                    ), listOf(message.from)
                )
            } else {
                if (token.userId !in listOf(message.from, message.to)) return error("权限错误")
                messageObserverDao.addOrUpdate(messageObserver)
                ChatUtil.sendMessage(
                    MessageVO(
                        message.id, false, token.userId, message.getOtherParty(token.userId), Content(
                            "OBSERVER", value = ""
                        )
                    )
                )
            }
        } catch (_: SQLIntegrityConstraintViolationException) {
        }
        return success()
    }
}