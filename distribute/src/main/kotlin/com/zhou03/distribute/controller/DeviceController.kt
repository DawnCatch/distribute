package com.zhou03.distribute.controller

import com.zhou03.distribute.dto.DeviceCheckDTO
import com.zhou03.distribute.dto.DeviceDeleteDTO
import com.zhou03.distribute.dto.DeviceGenerateDTO
import com.zhou03.distribute.dto.DeviceModifyDTO
import com.zhou03.distribute.service.DeviceService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/device")
@RestController
class DeviceController {

    @Autowired
    lateinit var deviceService: DeviceService

    @RequestMapping("/generate")
    fun generate(
        @RequestBody deviceGenerateDTO: DeviceGenerateDTO,
        request: HttpServletRequest,
    ) = deviceService.generate(deviceGenerateDTO, request)

    @RequestMapping("/modify")
    fun modify(
        @RequestBody deviceModifyDTO: DeviceModifyDTO,
        request: HttpServletRequest,
    ) = deviceService.modify(deviceModifyDTO, request)

    @RequestMapping("/delete")
    fun delete(
        @RequestBody deviceDeleteDTO: DeviceDeleteDTO,
        request: HttpServletRequest,
    ) = deviceService.delete(deviceDeleteDTO, request)

    @RequestMapping("/list")
    fun list(
        request: HttpServletRequest,
    ) = deviceService.list(request)

    @RequestMapping("/check")
    fun check(
        @RequestBody deviceCheckDTO: DeviceCheckDTO,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) = deviceService.check(deviceCheckDTO, response)
}