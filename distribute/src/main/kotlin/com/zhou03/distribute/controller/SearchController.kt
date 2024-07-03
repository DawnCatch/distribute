package com.zhou03.distribute.controller

import com.zhou03.distribute.dto.search.SearchAllDTO
import com.zhou03.distribute.service.SearchService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class SearchController {

    @Autowired
    lateinit var searchService: SearchService

    @RequestMapping("/all")
    fun all(
        @RequestBody searchAllDTO: SearchAllDTO,
        request: HttpServletRequest,
    ) = searchService.searchByTitle(searchAllDTO.title, request)
}
