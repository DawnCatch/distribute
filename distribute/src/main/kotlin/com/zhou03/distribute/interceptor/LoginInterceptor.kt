package com.zhou03.distribute.interceptor

import com.zhou03.distribute.dao.DeviceDao
import com.zhou03.distribute.dao.UserDao
import com.zhou03.distribute.domain.Auth
import com.zhou03.distribute.util.setUser
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
        var user = userDao.check(request.cookies)
        val device = deviceDao.check(request.cookies)
        if (user == null && device == null) return false
        if (user != null) {
            request.setUser(user)
            return true
        } else if (device != null) {
            user = userDao.getById(device.userId) ?: return false
            request.setUser(user.changeAuth(Auth.DEVICE))
            return true
        } else return false
    }
}