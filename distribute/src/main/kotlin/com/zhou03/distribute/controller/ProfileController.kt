package com.zhou03.distribute.controller

import com.zhou03.distribute.dto.profile.ProfileListByIdDTO
import com.zhou03.distribute.service.ProfileService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/profile")
@RestController
class ProfileController {

    @Autowired
    lateinit var profileService: ProfileService

    @RequestMapping("/get/{id}")
    fun getById(
        @PathVariable("id") id: Int,
        request: HttpServletRequest,
    ) = profileService.getById(id, request)

    @RequestMapping("/list")
    fun list(
        @RequestBody profileListByIdDTO: ProfileListByIdDTO,
        request: HttpServletRequest,
    ) = profileService.listById(profileListByIdDTO, request)

    @RequestMapping("/upload/avatar")
    fun uploadAvatar(
        file: MultipartFile,
        request: HttpServletRequest,
    ) = profileService.uploadAvatar(file, request)
}