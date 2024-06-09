package com.zhou03.distribute.service

import com.zhou03.distribute.dao.GroupDao
import com.zhou03.distribute.dao.GroupUserRelationDao
import com.zhou03.distribute.dao.ProfileDao
import com.zhou03.distribute.dao.UserRelationDao
import com.zhou03.distribute.domain.GroupRole
import com.zhou03.distribute.domain.GroupUserRelation
import com.zhou03.distribute.domain.UserRelation
import com.zhou03.distribute.dto.relation.RelationApplicationDTO
import com.zhou03.distribute.dto.relation.RelationHandleDTO
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.util.toLocalDateTime
import com.zhou03.distribute.vo.PendRelationVO
import com.zhou03.distribute.vo.Result
import com.zhou03.distribute.vo.error
import com.zhou03.distribute.vo.success
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface RelationService {

    fun userFollow(relationFollowDTO: RelationApplicationDTO, request: HttpServletRequest): Result<Nothing?>

    fun groupApplication(relationFollowDTO: RelationApplicationDTO, request: HttpServletRequest): Result<Nothing?>

    fun listGroupPendingHandle(request: HttpServletRequest): Result<List<PendRelationVO>?>

    fun handle(relationHandleDTO: RelationHandleDTO, request: HttpServletRequest): Result<Nothing?>

    fun listFollow(request: HttpServletRequest): Result<List<Int>?>

    fun listFan(request: HttpServletRequest): Result<List<Int>?>

    fun listGroup(request: HttpServletRequest): Result<List<Int>?>

    fun listOwnApplication(request: HttpServletRequest): Result<List<Int>?>
}

@Service
class RelationServiceImpl : RelationService {

    @Autowired
    lateinit var userRelationDao: UserRelationDao

    @Autowired
    lateinit var groupUserRelationDao: GroupUserRelationDao

    @Autowired
    lateinit var profileDao: ProfileDao

    @Autowired
    lateinit var groupDao: GroupDao

    override fun userFollow(
        relationFollowDTO: RelationApplicationDTO,
        request: HttpServletRequest,
    ): Result<Nothing?> {
        val token = request.getToken()
        val profile = profileDao.getById(relationFollowDTO.targetId) ?: return error("查无此人")
        val status = !userRelationDao.isRelation(token.userId, relationFollowDTO.targetId)
        val relation = UserRelation().apply {
            this.userId = token.userId
            this.targetId = relationFollowDTO.targetId
            this.status = status
            this.path = "/"
            this.nickname = profile.nickname
            this.date = LocalDateTime.now()
        }
        userRelationDao.addOrUpdate(relation)
        return success(message = if (status) "关注成功" else "取消关注")
    }

    override fun groupApplication(
        relationFollowDTO: RelationApplicationDTO,
        request: HttpServletRequest,
    ): Result<Nothing?> {
        val token = request.getToken()
        if (groupUserRelationDao.isRelation(token.userId, relationFollowDTO.targetId)) return error("已存在联系")
        var relation = groupUserRelationDao.getApplicationAsOwn(token.userId, relationFollowDTO.targetId)
        if (relation != null) {
            relation.apply {
                this.date = 0L.toLocalDateTime()
                flushChanges()
            }
            return error("取消成功")
        }
        val group = groupDao.getById(relationFollowDTO.targetId) ?: return error("查无此项")
        if (!group.visible) return error("权限错误")
        relation = GroupUserRelation().apply {
            this.userId = token.userId
            this.targetId = relationFollowDTO.targetId
            this.status = false
            this.path = "/"
            this.nickname = group.title
            this.date = LocalDateTime.now()
        }
        groupUserRelationDao.addOrUpdate(relation)
        return success(message = "申请成功")
    }

    override fun listGroupPendingHandle(request: HttpServletRequest): Result<List<PendRelationVO>?> {
        val token = request.getToken()
        val managedGroupIds = groupUserRelationDao.listByMeAsAManager(token.userId).map { it.targetId }
        val pendingRelations = groupUserRelationDao.listPending(managedGroupIds)
        return success(pendingRelations.map { PendRelationVO.from(it) })
    }

    override fun handle(relationHandleDTO: RelationHandleDTO, request: HttpServletRequest): Result<Nothing?> {
        val token = request.getToken()
        val relation = groupUserRelationDao.getById(relationHandleDTO.id) ?: return error("处理失败")
        if (!groupUserRelationDao.isManager(
                token.userId, relation.targetId
            )
        ) return error("权限错误")
        if (groupUserRelationDao.isRelation(
                relation.userId, relation.targetId
            )
        ) return error(message = "已存在联系")
        relation.apply {
            this.status = true
            this.date = LocalDateTime.now()
            this.role = GroupRole.MEMBER
            flushChanges()
        }
        return success(null, "处理完毕")
    }

    override fun listFollow(request: HttpServletRequest): Result<List<Int>?> {
        val token = request.getToken()
        val follows = userRelationDao.listFollowAsOwn(token.userId)
        return success(follows.map { it.targetId })
    }

    override fun listFan(request: HttpServletRequest): Result<List<Int>?> {
        val token = request.getToken()
        val fans = userRelationDao.listFanAsOwn(token.userId)
        return success(fans.map { it.userId })
    }

    override fun listGroup(request: HttpServletRequest): Result<List<Int>?> {
        val token = request.getToken()
        val relations = groupUserRelationDao.listByJoinAsOwn(token.userId)
        return success(relations.map { it.targetId })
    }

    override fun listOwnApplication(request: HttpServletRequest): Result<List<Int>?> {
        val token = request.getToken()
        val relations = groupUserRelationDao.listByPendingAsOwn(token.userId)
        return success(relations.map { it.targetId })
    }
}