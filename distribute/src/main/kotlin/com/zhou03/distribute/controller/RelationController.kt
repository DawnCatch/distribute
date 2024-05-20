package com.zhou03.distribute.controller

import com.zhou03.distribute.dto.RelationApplicationDTO
import com.zhou03.distribute.dto.RelationHandleDTO
import com.zhou03.distribute.service.RelationService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/relation")
class RelationController {

    @Autowired
    lateinit var relationService: RelationService

    @RequestMapping("/application")
    fun application(
        @RequestBody relationApplicationDTO: RelationApplicationDTO,
        request: HttpServletRequest,
    ) = relationService.application(relationApplicationDTO, request)

    @RequestMapping("/handle")
    fun handle(
        @RequestBody relationHandleDTO: RelationHandleDTO,
        request: HttpServletRequest,
    ) = relationService.handle(relationHandleDTO, request)

    @RequestMapping("/list")
    fun list(
        request: HttpServletRequest,
    ) = relationService.getRelations(request)

    @RequestMapping("/list/application")
    fun listApplication(
        request: HttpServletRequest,
    ) = relationService.getApplications(request)
}