package com.zhou03.distribute.service

import com.zhou03.distribute.dao.GroupDao
import com.zhou03.distribute.dao.UserDao
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.vo.Result
import com.zhou03.distribute.vo.SearchVO
import com.zhou03.distribute.vo.success
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface SearchService {

    fun searchByTitle(title: String, request: HttpServletRequest): Result<List<SearchVO>?>
}

@Service
class SearchServiceImpl : SearchService {

    @Autowired
    lateinit var userDao: UserDao

    @Autowired
    lateinit var groupDao: GroupDao

    override fun searchByTitle(title: String, request: HttpServletRequest): Result<List<SearchVO>?> {
        if (title == "") return success(listOf())
        val token = request.getToken()
        val users = userDao.likeByUsernameOrEmail(title).filter { it.id != token.userId }
        val groups = groupDao.likeByTitle(title)
        val result = users.map { SearchVO.from(it) } + groups.map { SearchVO.from(it) }
        return success(result)
    }
}