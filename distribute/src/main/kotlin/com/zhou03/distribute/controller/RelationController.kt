package com.zhou03.distribute.controller

import com.zhou03.distribute.dto.relation.RelationApplicationDTO
import com.zhou03.distribute.dto.relation.RelationHandleDTO
import com.zhou03.distribute.dto.relation.RelationQueryDTO
import com.zhou03.distribute.service.RelationService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/relation")
class RelationController {

    @Autowired
    lateinit var relationService: RelationService

    @RequestMapping("/get")
    fun getRelation(
        @RequestBody relationQueryDTO: RelationQueryDTO,
        request: HttpServletRequest,
    ) = relationService.getRelation(relationQueryDTO, request)

    @RequestMapping("/user/follow")
    fun userApplication(
        @RequestBody relationApplicationDTO: RelationApplicationDTO,
        request: HttpServletRequest,
    ) = relationService.userFollow(relationApplicationDTO, request)

    @RequestMapping("/group/application")
    fun groupApplication(
        @RequestBody relationApplicationDTO: RelationApplicationDTO,
        request: HttpServletRequest,
    ) = relationService.groupApplication(relationApplicationDTO, request)

    @RequestMapping("/list/pending")
    fun listGroupPendingHandle(
        request: HttpServletRequest,
    ) = relationService.listGroupPendingHandle(request)

    @RequestMapping("/handle")
    fun handle(
        @RequestBody relationHandleDTO: RelationHandleDTO,
        request: HttpServletRequest,
    ) = relationService.handle(relationHandleDTO, request)

    @RequestMapping("/list/follow")
    fun listFollow(
        request: HttpServletRequest,
    ) = relationService.listFollow(request)

    @RequestMapping("/list/fan")
    fun listFan(
        request: HttpServletRequest,
    ) = relationService.listFan(request)

    @RequestMapping("/list/group")
    fun listGroup(
        request: HttpServletRequest,
    ) = relationService.listGroup(request)

    @RequestMapping("/list/own/application")
    fun listOwnApplication(
        request: HttpServletRequest,
    ) = relationService.listOwnApplication(request)

    @RequestMapping("/list/union")
    fun listUnion(
        request: HttpServletRequest,
    ) = relationService.listUnion(request)

    @RequestMapping("/get/member/{groupId}")
    fun getMember(
        @PathVariable("groupId") groupId: Int,
        request: HttpServletRequest,
    ) = relationService.listMemberByGroup(groupId, request)

    @RequestMapping("/len/member/{groupId}")
    fun lenMember(
        @PathVariable("groupId") groupId: Int,
        request: HttpServletRequest,
    ) = relationService.getLenOfMemberByGroup(groupId, request)
}