package com.zhou03.distribute.service

import com.zhou03.distribute.dao.GroupDao
import com.zhou03.distribute.dao.ProfileDao
import com.zhou03.distribute.dao.RelationDao
import com.zhou03.distribute.domain.Relation
import com.zhou03.distribute.dto.relation.RelationApplicationDTO
import com.zhou03.distribute.dto.relation.RelationHandleDTO
import com.zhou03.distribute.dto.relation.RelationSearchDTO
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

    fun userApplication(relationFollowDTO: RelationApplicationDTO, request: HttpServletRequest): Result<Nothing?>

    fun groupApplication(relationFollowDTO: RelationApplicationDTO, request: HttpServletRequest): Result<Nothing?>

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

    @Autowired
    lateinit var groupDao: GroupDao

    fun application(
        type: Boolean,
        relationFollowDTO: RelationApplicationDTO,
        request: HttpServletRequest,
    ): Result<Nothing?> {
        val token = request.getToken()
        if (!type && relationFollowDTO.targetId == token.userId) return error("无法向自己发送申请")
        var relation = relationDao.getIdAndId(type, token.userId, relationFollowDTO.targetId)
        if (relation == null) {
            relation = Relation().apply {
                this.type = type
                this.userId = token.userId
                this.targetId = relationFollowDTO.targetId
                this.status = false
                this.date = LocalDateTime.now()
                this.path = "/"
            }
            relationDao.add(relation)
        } else if (relation.status) {
            return error(if (type) "已加入群组" else "已添加好友")
        } else {
            relation.apply {
                this.date = LocalDateTime.now()
                flushChanges()
            }
        }
        return success(null, "申请成功")
    }

    override fun userApplication(
        relationFollowDTO: RelationApplicationDTO,
        request: HttpServletRequest,
    ) = application(false, relationFollowDTO, request)

    override fun groupApplication(
        relationFollowDTO: RelationApplicationDTO,
        request: HttpServletRequest,
    ) = application(true, relationFollowDTO, request)

    override fun handle(relationHandleDTO: RelationHandleDTO, request: HttpServletRequest): Result<Nothing?> {
        val token = request.getToken()
        var relation = relationDao.getApplicationById(relationHandleDTO.id) ?: return error("权限错误")
        if (!relation.type && relation.targetId != token.userId) return error("权限错误")
        if (relation.type && !groupDao.check(token.userId)) return error("权限错误")
        relation.apply {
            if (relationHandleDTO.status) {
                this.date = LocalDateTime.now()
            } else {
                this.date = 0L.toLocalDateTime()
            }
            this.status = relationHandleDTO.status
            flushChanges()
        }
        if (!relation.type && relationHandleDTO.status) {
            relation = Relation().apply {
                this.userId = relation.targetId
                this.targetId = relation.userId
                this.date = relation.date
                this.status = true
                this.path = "/"
            }
            relationDao.add(relation)
        }
        if (relationHandleDTO.status) {
            ChatUtil.sendMessage(
                MessageVO.create(
                    relation.targetId, relation.userId, Content("TEXT", "你好!")
                )
            )
        }
        return success(null, "处理完毕")
    }

    override fun getRelations(request: HttpServletRequest): Result<List<RelationVO>?> {
        val token = request.getToken()
        val relations = relationDao.listRelation(false, token.userId)
        if (relations.isEmpty()) return success(listOf())
        val ids = relations.map {
            it.targetId
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
        val ownGroups = groupDao.listAsOwn(token.userId)
        val groupIds = ownGroups.map { it.id }
        val userApplications = relationDao.listUserApplication(token.userId)
        val groupApplications = relationDao.listGroupApplication(groupIds)
        val applications = userApplications + groupApplications
        if (applications.isEmpty()) return success(listOf())
        val ids = applications.map {
            it.userId
        }
        val groupsMap = ownGroups.groupBy { it.id }
        val profilesMap = profileDao.listById(ids).groupBy { it.userId }
        val result = mutableListOf<ApplicationVO>()
        if (profilesMap.isEmpty()) return success(result)
        applications.forEach { application ->
            val profiles = profilesMap[application.userId]
            if (profiles.isNullOrEmpty()) return@forEach
            if (application.type) {
                val groups = groupsMap[application.targetId]
                if (groups.isNullOrEmpty()) return@forEach
                result += ApplicationVO(
                    application.id,
                    application.type,
                    application.userId,
                    application.targetId,
                    profiles[0].nickname,
                    groups[0].title,
                    application.date.toMilliSecond()
                )
            } else {
                result += ApplicationVO(
                    application.id,
                    application.type,
                    application.userId,
                    application.targetId,
                    profiles[0].nickname,
                    "",
                    application.date.toMilliSecond()
                )
            }
        }
        return success(result)
    }

    override fun search(
        relationSearchDTO: RelationSearchDTO,
        request: HttpServletRequest,
    ): Result<List<RelationSearchVO>?> {

        return success(listOf())
    }

}