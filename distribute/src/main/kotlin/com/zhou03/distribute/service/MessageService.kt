package com.zhou03.distribute.service

import com.zhou03.distribute.dao.DeviceDao
import com.zhou03.distribute.dao.MessageDao
import com.zhou03.distribute.dao.RelationDao
import com.zhou03.distribute.domain.Message
import com.zhou03.distribute.dto.MessageHistoryDTO
import com.zhou03.distribute.dto.MessageSendDTO
import com.zhou03.distribute.util.ChatUtil
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.util.toJson
import com.zhou03.distribute.util.toLocalDateTime
import com.zhou03.distribute.vo.MessageVO
import com.zhou03.distribute.vo.Result
import com.zhou03.distribute.vo.error
import com.zhou03.distribute.vo.success
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface MessageService {

    fun history(messageHistoryDTO: MessageHistoryDTO, request: HttpServletRequest): Result<List<MessageVO>?>

    fun send(messageSendDTO: MessageSendDTO, request: HttpServletRequest): Result<Nothing?>

    fun send(key: String, messageSendDTO: MessageSendDTO): Result<Nothing?>
}

@Service
class MessageServiceImpl : MessageService {

    @Autowired
    lateinit var messageDao: MessageDao

    @Autowired
    lateinit var deviceDao: DeviceDao

    @Autowired
    lateinit var relationDao: RelationDao

    override fun history(messageHistoryDTO: MessageHistoryDTO, request: HttpServletRequest): Result<List<MessageVO>?> {
        val token = request.getToken()
        val from = messageHistoryDTO.from.toLocalDateTime()
        val to = messageHistoryDTO.to.toLocalDateTime()
        val messages = messageDao.getListOfDate(token.userId, from, to).map {
            MessageVO.from(it)
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
        val message = MessageVO.from(Message().apply {
            from = token.userId
            to = messageSendDTO.to
            content = toJson(messageSendDTO.content)
            date = LocalDateTime.now()
        })
        ChatUtil.sendMessage(message.to())
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
        val message = MessageVO.from(Message().apply {
            from = device.userId
            to = if (messageSendDTO.to == -1) device.userId else messageSendDTO.to
            content = toJson(messageSendDTO.content)
            date = LocalDateTime.now()
        })
        ChatUtil.sendMessage(message.to())
        return success(null, "发送成功")
    }
}