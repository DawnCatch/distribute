package com.zhou03.distribute.service

import com.zhou03.distribute.dao.ProfileDao
import com.zhou03.distribute.dao.RelationDao
import com.zhou03.distribute.domain.Relation
import com.zhou03.distribute.dto.RelationApplicationDTO
import com.zhou03.distribute.dto.RelationHandleDTO
import com.zhou03.distribute.dto.RelationSearchDTO
import com.zhou03.distribute.util.ChatUtil
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.util.toLocalDateTime
import com.zhou03.distribute.util.toMilliSecond
import com.zhou03.distribute.vo.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface RelationService {

    fun application(relationFollowDTO: RelationApplicationDTO, request: HttpServletRequest): Result<Nothing?>

    fun handle(relationHandleDTO: RelationHandleDTO, request: HttpServletRequest): Result<Nothing?>

    fun getRelations(request: HttpServletRequest): Result<List<RelationVO>?>

    fun getApplications(request: HttpServletRequest): Result<List<ApplicationVO>?>

    fun search(relationSearchDTO: RelationSearchDTO, request: HttpServletRequest): Result<List<RelationSearchVO>?>

}

@Service
class RelationServiceImpl : RelationService {

    @Autowired
    lateinit var relationDao: RelationDao

    @Autowired
    lateinit var profileDao: ProfileDao

    override fun application(relationFollowDTO: RelationApplicationDTO, request: HttpServletRequest): Result<Nothing?> {
        val token = request.getToken()
        var relation = relationDao.getIdAndId(token.userId, relationFollowDTO.userId)
        if (relation == null) {
            relation = Relation().apply {
                this.userId = token.userId
                this.tagetId = relationFollowDTO.userId
                this.status = false
                this.date = LocalDateTime.now()
            }
            relationDao.add(relation)
        } else {
            relation.apply {
                this.date = LocalDateTime.now()
                flushChanges()
            }
        }
        return success(null, "申请成功")
    }

    override fun handle(relationHandleDTO: RelationHandleDTO, request: HttpServletRequest): Result<Nothing?> {
        val token = request.getToken()
        var relation = relationDao.getById(relationHandleDTO.id) ?: return error("操作错误")
        if (relation.tagetId != token.userId) return error("操作错误")
        relation.apply {
            if (relationHandleDTO.status) {
                this.date = LocalDateTime.now()
            } else {
                this.date = 0L.toLocalDateTime()
            }
            this.status = relationHandleDTO.status
            flushChanges()
        }
        if (relationHandleDTO.status) {
            relation = Relation().apply {
                this.userId = relation.tagetId
                this.tagetId = relation.userId
                this.date = relation.date
                this.status = true
            }
            relationDao.add(relation)
        }
        if (relationHandleDTO.status) {
            ChatUtil.sendMessage(
                MessageVO.create(
                    relation.tagetId, relation.userId, Content("TEXT", "你好!")
                )
            )

        }
        return success(null, "处理完毕")
    }

    override fun getRelations(request: HttpServletRequest): Result<List<RelationVO>?> {
        val token = request.getToken()
        val relations = relationDao.listRelation(token.userId)
        if (relations.isEmpty()) return success(listOf())
        val ids = relations.map {
            it.tagetId
        }
        val profiles = profileDao.listById(ids)
        val result = mutableListOf<RelationVO>()
        for (i in profiles.indices) {
            result += RelationVO(profiles[i].id, profiles[i].nickname, relations[i].path)
        }
        return success(result)
    }

    override fun getApplications(request: HttpServletRequest): Result<List<ApplicationVO>?> {
        val token = request.getToken()
        val applications = relationDao.listApplication(token.userId)
        if (applications.isEmpty()) return success(listOf())
        val ids = applications.map {
            it.userId
        }
        val profiles = profileDao.listById(ids)
        val result = mutableListOf<ApplicationVO>()
        for (i in profiles.indices) {
            result += ApplicationVO(profiles[i].id, profiles[i].nickname, applications[i].date.toMilliSecond())
        }
        return success(result)
    }

    override fun search(
        relationSearchDTO: RelationSearchDTO,
        request: HttpServletRequest,
    ): Result<List<RelationSearchVO>?> {
        val token = request.getToken()
        return success(listOf())
    }

}