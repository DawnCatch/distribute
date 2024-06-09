package com.zhou03.distribute.interceptor

import com.zhou03.distribute.dao.DeviceDao
import com.zhou03.distribute.dao.UserDao
import com.zhou03.distribute.util.*
import com.zhou03.distribute.util.TokenUtil.SUBJECT
import com.zhou03.distribute.vo.error
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.io.PrintWriter

@Component
class LoginInterceptor : HandlerInterceptor {

    @Autowired
    lateinit var userDao: UserDao

    @Autowired
    lateinit var deviceDao: DeviceDao

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authorization: String
        try {
            authorization = request.getHeader(SUBJECT).refreshToken()
        } catch (_: NullPointerException) {
            return error(response)
        }
        if (authorization == request.getHeader(SUBJECT)) return error(response)
        val claims: Claims
        try {
            claims = TokenUtil.parsePayload(authorization)
        } catch (_: SignatureException) {
            //密钥被篡改
            return error(response)
        } catch (_: ExpiredJwtException) {
            //密钥过期
            return error(response)
        }
        val token = claims.getToken() ?: return false
        request.setToken(token)
        response.setHeader(SUBJECT, authorization)
        return true
    }

    fun error(response: HttpServletResponse): Boolean {
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        val out: PrintWriter = response.writer
        out.print(toJson(error<Nothing>("请先登录")))
        out.flush()
        out.close()
        return false
    }
}