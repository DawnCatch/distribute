package com.zhou03.distribute.controller

import com.zhou03.distribute.dto.group.GroupCreateDTO
import com.zhou03.distribute.dto.group.GroupDeleteDTO
import com.zhou03.distribute.dto.group.GroupModifyDTO
import com.zhou03.distribute.service.GroupService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/group")
@RestController
class GroupController {

    @Autowired
    lateinit var groupService: GroupService

    @RequestMapping("/create")
    fun create(
        @RequestBody groupCreateDTO: GroupCreateDTO,
        request: HttpServletRequest,
    ) = groupService.create(groupCreateDTO, request)

    @RequestMapping("/modify")
    fun modify(
        @RequestBody groupModifyDTO: GroupModifyDTO,
        request: HttpServletRequest,
    ) = groupService.modify(groupModifyDTO, request)

    @RequestMapping("/delete")
    fun delete(
        @RequestBody groupDeleteDTO: GroupDeleteDTO,
        request: HttpServletRequest,
    ) = groupService.delete(groupDeleteDTO, request)

    @RequestMapping("/get/{id}")
    fun get(
        @PathVariable("id") id: Int,
        request: HttpServletRequest,
    ) = groupService.get(id,request)
}