package com.zhou03.distribute.util

import com.zhou03.distribute.dao.MessageDao
import com.zhou03.distribute.vo.MessageVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Component
object ChatUtil {

    private val sessionsMap: ConcurrentHashMap<Int, MutableList<WebSocketSession>> = ConcurrentHashMap()

    private fun put(userId: Int, session: WebSocketSession) {
        val sessions = sessionsMap[userId]
        if (sessions == null) {
            sessionsMap[userId] = mutableListOf(session)
        } else {
            sessions += session
        }
    }

    fun put(session: WebSocketSession) {
        val token = session.getToken()
        put(token.userId, session)
    }

    private fun remove(userId: Int, session: WebSocketSession) {
        val sessions = sessionsMap[userId] ?: return
        sessions.remove(session)
    }

    fun remove(session: WebSocketSession) {
        val token = session.getToken()
        remove(token.userId, session)
    }

    private lateinit var messageDao: MessageDao

    @Autowired
    fun setMessageDao(messageDao: MessageDao) {
        this.messageDao = messageDao
    }

    fun sendMessage(webSocketMessage: WebSocketMessage<*>) {
        val messageDomain = MessageVO.from(webSocketMessage).toDomain()
        messageDao.add(messageDomain)
        val message = MessageVO.from(messageDomain)
        sessionsMap.forEach { (userId, sessions) ->
            if (userId != message.to && userId != message.from) return@forEach
            sessions.forEach { session ->
                try {
                    if (session.isOpen) session.sendMessage(message.to())
                    else remove(session)
                } catch (e: Exception) {
                    remove(session)
                }
            }
        }
    }

    fun sendMessage(message: MessageVO) {
        val messageDomain = message.toDomain()
        messageDao.add(messageDomain)
        val messageVO = MessageVO.from(messageDomain)
        sessionsMap.forEach { (userId, sessions) ->
            if (userId != message.to && userId != message.from) return@forEach
            sessions.forEach { session ->
                try {
                    if (session.isOpen) session.sendMessage(messageVO.to())
                    else remove(session)
                } catch (e: Exception) {
                    remove(session)
                }
            }
        }
    }
}