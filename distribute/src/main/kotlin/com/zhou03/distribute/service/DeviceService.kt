package com.zhou03.distribute.service

import com.zhou03.distribute.dao.DeviceDao
import com.zhou03.distribute.dao.ProfileDao
import com.zhou03.distribute.domain.Auth
import com.zhou03.distribute.domain.Device
import com.zhou03.distribute.dto.DeviceCheckDTO
import com.zhou03.distribute.dto.DeviceDeleteDTO
import com.zhou03.distribute.dto.DeviceGenerateDTO
import com.zhou03.distribute.dto.DeviceModifyDTO
import com.zhou03.distribute.util.aesEncrypt
import com.zhou03.distribute.util.device
import com.zhou03.distribute.util.getUser
import com.zhou03.distribute.util.uuid
import com.zhou03.distribute.vo.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface DeviceService {

    fun generate(deviceGenerateDTO: DeviceGenerateDTO, request: HttpServletRequest): Result<DeviceOnceVO?>

    fun modify(deviceModifyDTO: DeviceModifyDTO, request: HttpServletRequest): Result<DeviceVO?>

    fun delete(deviceDeleteDTO: DeviceDeleteDTO, request: HttpServletRequest): Result<DeviceVO?>

    fun list(request: HttpServletRequest): Result<List<DeviceVO>?>

    fun check(
        deviceCheckDTO: DeviceCheckDTO,
        response: HttpServletResponse,
    ): Result<ProfileVO?>
}

@Service
class DeviceServiceImpl : DeviceService {

    @Autowired
    lateinit var deviceDao: DeviceDao

    @Autowired
    lateinit var profileDao: ProfileDao

    override fun generate(deviceGenerateDTO: DeviceGenerateDTO, request: HttpServletRequest): Result<DeviceOnceVO?> {
        val user = request.getUser()
        if (user.auth < Auth.USER) return error("权限错误")
        val device = Device().apply {
            this.userId = user.id
            this.title = deviceGenerateDTO.title
            this.code = uuid().aesEncrypt()
            this.status = true
        }
        deviceDao.add(device)
        return success(DeviceOnceVO.from(device))
    }

    override fun modify(deviceModifyDTO: DeviceModifyDTO, request: HttpServletRequest): Result<DeviceVO?> {
        val user = request.getUser()
        if (user.auth < Auth.USER) return error("权限错误")
        val device = deviceDao.getOwnById(deviceModifyDTO.id, user.id) ?: return error("修改失败")
        device.apply {
            this.title = deviceModifyDTO.title
            flushChanges()
        }
        return success(DeviceVO.from(device))
    }

    override fun delete(deviceDeleteDTO: DeviceDeleteDTO, request: HttpServletRequest): Result<DeviceVO?> {
        val user = request.getUser()
        if (user.auth < Auth.USER) return error("权限错误")
        val device = deviceDao.getOwnById(deviceDeleteDTO.id, user.id) ?: return error("删除失败")
        device.apply {
            this.status = false
            flushChanges()
        }
        return success()
    }

    override fun list(request: HttpServletRequest): Result<List<DeviceVO>?> {
        val user = request.getUser()
        if (user.auth < Auth.USER) return error("权限错误")
        val devices = deviceDao.listOwn(user.id)
        return success(devices.map { DeviceVO.from(it) })
    }

    override fun check(
        deviceCheckDTO: DeviceCheckDTO,
        response: HttpServletResponse,
    ): Result<ProfileVO?> {
        val device = deviceDao.check(deviceCheckDTO.code) ?: return error("验证失败")
        response.addCookie(device(device))
        val profile = profileDao.getById(device.userId)
        return success(ProfileVO.from(profile!!))
    }
}