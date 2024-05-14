package com.zhou03.distribute.controller

import com.zhou03.distribute.dto.UserLoginDTO
import com.zhou03.distribute.dto.UserRegisterDTO
import com.zhou03.distribute.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/user")
@RestController
class UserController {

    @Autowired
    lateinit var userService: UserService

    @RequestMapping("/login")
    fun login(
        @RequestBody userLoginDTO: UserLoginDTO,
        response: HttpServletResponse,
    ) = userService.login(userLoginDTO, response)

    @RequestMapping("/register")
    fun register(
        @RequestBody userRegisterDTO: UserRegisterDTO,
        response: HttpServletResponse,
    ) = userService.register(userRegisterDTO, response)

    @RequestMapping("/reconnect")
    fun reconnection(
        request: HttpServletRequest,
    ) = userService.reconnect(request)
}