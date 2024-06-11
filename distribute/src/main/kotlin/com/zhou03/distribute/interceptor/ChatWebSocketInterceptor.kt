package com.zhou03.distribute.interceptor

import com.zhou03.distribute.dao.UserDao
import com.zhou03.distribute.util.TokenUtil
import com.zhou03.distribute.util.getHeader
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.util.setHeader
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

class ChatWebSocketInterceptor : HandshakeInterceptor {

    @Autowired
    lateinit var userDao: UserDao

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>,
    ): Boolean {
        val authorization = request.getHeader(HEADER_KEY)
        val claims: Claims
        try {
            claims = TokenUtil.parsePayload(authorization)
        } catch (_: SignatureException) {
            //密钥被篡改
            return false
        } catch (_: ExpiredJwtException) {
            //密钥过期
            return false
        }
        val token = claims.getToken() ?: return false
        attributes["token"] = token
        response.setHeader(HEADER_KEY, authorization)
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?,
    ) {

    }

    companion object {
        const val HEADER_KEY = "Sec-Websocket-Protocol"
    }
}