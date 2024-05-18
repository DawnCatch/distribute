package com.zhou03.distribute.interceptor

import com.zhou03.distribute.dao.DeviceDao
import com.zhou03.distribute.dao.UserDao
import com.zhou03.distribute.util.TokenUtil
import com.zhou03.distribute.util.TokenUtil.SUBJECT
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.util.refreshToken
import com.zhou03.distribute.util.setToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class LoginInterceptor : HandlerInterceptor {

    @Autowired
    lateinit var userDao: UserDao

    @Autowired
    lateinit var deviceDao: DeviceDao

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authorization = request.getHeader(SUBJECT).refreshToken()
        if (authorization == request.getHeader(SUBJECT)) return false
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
        request.setToken(token)
        response.setHeader(SUBJECT, authorization)
        return true
    }
}