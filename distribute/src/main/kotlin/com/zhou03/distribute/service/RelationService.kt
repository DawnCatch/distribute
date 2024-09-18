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
import com.zhou03.distribute.dto.relation.RelationQueryDTO
import com.zhou03.distribute.util.ChatUtil
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.util.toLocalDateTime
import com.zhou03.distribute.vo.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface RelationService {

    fun getRelation(relationQueryDTO: RelationQueryDTO, request: HttpServletRequest): Result<RelationVO?>

    fun getRelationByTargetId(relationQueryDTO: RelationQueryDTO, request: HttpServletRequest): Result<RelationVO?>

    fun userFollow(relationFollowDTO: RelationApplicationDTO, request: HttpServletRequest): Result<Nothing?>

    fun groupApplication(relationApplicationDTO: RelationApplicationDTO, request: HttpServletRequest): Result<Nothing?>

    fun listGroupPendingHandle(request: HttpServletRequest): Result<List<Int>?>

    fun handle(relationHandleDTO: RelationHandleDTO, request: HttpServletRequest): Result<Nothing?>

    fun listFollow(request: HttpServletRequest): Result<List<Int>?>

    fun listFan(request: HttpServletRequest): Result<List<Int>?>

    fun listGroup(request: HttpServletRequest): Result<List<Int>?>

    fun listOwnApplication(request: HttpServletRequest): Result<List<Int>?>

    fun listUnion(request: HttpServletRequest): Result<RelationUnionVO?>

    fun listMemberByGroup(groupId: Int, request: HttpServletRequest): Result<List<Int>?>

    fun getLenOfMemberByGroup(groupId: Int, request: HttpServletRequest): Result<Int?>
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

    override fun getRelation(relationQueryDTO: RelationQueryDTO, request: HttpServletRequest): Result<RelationVO?> {
        val relationVO: RelationVO
        if (relationQueryDTO.type) {
            val relation = groupUserRelationDao.getById(relationQueryDTO.id) ?: return error("查无此项")
            val group = groupDao.getById(relation.targetId) ?: return error("查无此项")
            relationVO = RelationVO.from(relation, group)
        } else {
            val relation = userRelationDao.getById(relationQueryDTO.id) ?: return error("查无此项")
            val profile = profileDao.getById(relationQueryDTO.id) ?: return error("查无此项")
            relationVO = RelationVO.from(relation, profile)
        }
        return success(relationVO)
    }

    override fun getRelationByTargetId(
        relationQueryDTO: RelationQueryDTO,
        request: HttpServletRequest,
    ): Result<RelationVO?> {
        val token = request.getToken()
        val relationVO: RelationVO
        if (relationQueryDTO.type) {
            val relation = groupUserRelationDao.getByTargetIdAsOwn(relationQueryDTO.id, token.userId)
            val group = groupDao.getById(relationQueryDTO.id) ?: return error("查无此项")
            relationVO = RelationVO.from(relation, group)
        } else {
            val relation = userRelationDao.getByTargetIdAsOwn(relationQueryDTO.id, token.userId)
            val profile = profileDao.getById(relationQueryDTO.id) ?: return error("查无此项")
            relationVO = RelationVO.from(relation, profile)
        }
        return success(relationVO)
    }

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
        ChatUtil.notice(
            Content("RELATION:FAN", "${if (status) "+" else "-"}${token.userId}"), relationFollowDTO.targetId
        )
        return success(message = if (status) "关注成功" else "取消关注")
    }

    override fun groupApplication(
        relationApplicationDTO: RelationApplicationDTO,
        request: HttpServletRequest,
    ): Result<Nothing?> {
        val token = request.getToken()
        if (groupUserRelationDao.isRelation(token.userId, relationApplicationDTO.targetId)) return error("已存在联系")
        var relation = groupUserRelationDao.getApplicationAsOwn(token.userId, relationApplicationDTO.targetId)
        val group = groupDao.getById(relationApplicationDTO.targetId) ?: return error("查无此项")
        val ids = groupUserRelationDao.listUserIdByGroupAsManager(group.id)
        if (!group.visible) return error("权限错误")
        if (relation != null) {
            relation.apply {
                this.date = 0L.toLocalDateTime()
                flushChanges()
            }
            ChatUtil.notice(Content("RELATION:PENDING", "-${relation.id}"), ids)
            return success(message = "取消成功")
        }
        relation = GroupUserRelation().apply {
            this.userId = token.userId
            this.targetId = relationApplicationDTO.targetId
            this.status = false
            this.path = "/"
            this.nickname = group.title
            this.date = LocalDateTime.now()
        }
        groupUserRelationDao.addOrUpdate(relation)
        ChatUtil.notice(Content("RELATION:PENDING", "+${relation.id}"), ids)
        return success(message = "申请成功")
    }

    override fun listGroupPendingHandle(request: HttpServletRequest): Result<List<Int>?> {
        val token = request.getToken()
        val managedGroupIds = groupUserRelationDao.listByMeAsAManager(token.userId)
        val pendingRelations = groupUserRelationDao.listPending(managedGroupIds)
        return success(pendingRelations)
    }

    override fun handle(relationHandleDTO: RelationHandleDTO, request: HttpServletRequest): Result<Nothing?> {
        val token = request.getToken()
        val relation = groupUserRelationDao.getById(relationHandleDTO.id) ?: return error("查无此项")
        if (!groupUserRelationDao.isManager(
                token.userId, relation.targetId
            )
        ) return error("权限错误")
        if (groupUserRelationDao.isRelation(
                relation.userId, relation.targetId
            )
        ) return error(message = "已存在联系")
        var ids = groupUserRelationDao.listMemberIdByTargetId(relation.targetId)
        if (relationHandleDTO.status) {
            relation.apply {
                this.status = true
                this.date = LocalDateTime.now()
                this.role = GroupRole.MEMBER
                flushChanges()
            }
            ChatUtil.notice(Content("RELATION:MEMBER", "${relation.targetId}"), ids)
            ChatUtil.notice(Content("RELATION:GROUP", "+${relation.targetId}"), relation.userId)
        } else {
            relation.apply {
                this.date = 0L.toLocalDateTime()
                flushChanges()
            }
        }
        ChatUtil.notice(Content("RELATION:APPLICATION", "-${relation.id}"), relation.userId)
        ids = groupUserRelationDao.listUserIdByGroupAsManager(relation.targetId)
        ChatUtil.notice(Content("RELATION:PENDING", "-${relation.id}"), ids)
        return success(null, "处理完毕")
    }

    override fun listFollow(request: HttpServletRequest): Result<List<Int>?> {
        val token = request.getToken()
        val follows = userRelationDao.listFollowAsOwn(token.userId)
        return success(follows)
    }

    override fun listFan(request: HttpServletRequest): Result<List<Int>?> {
        val token = request.getToken()
        val fans = userRelationDao.listFanAsOwn(token.userId)
        return success(fans)
    }

    override fun listGroup(request: HttpServletRequest): Result<List<Int>?> {
        val token = request.getToken()
        val groups = groupUserRelationDao.listByJoinAsOwn(token.userId)
        return success(groups)
    }

    override fun listOwnApplication(request: HttpServletRequest): Result<List<Int>?> {
        val token = request.getToken()
        val applications = groupUserRelationDao.listByPendingAsOwn(token.userId)
        return success(applications)
    }

    override fun listUnion(request: HttpServletRequest): Result<RelationUnionVO?> {
        val token = request.getToken()
        val follows = userRelationDao.listFollowAsOwn(token.userId)
        val fans = userRelationDao.listFanAsOwn(token.userId)
        val groups = groupUserRelationDao.listByJoinAsOwn(token.userId)
        val applications = groupUserRelationDao.listByPendingAsOwn(token.userId)
        val managedGroupIds = groupUserRelationDao.listByMeAsAManager(token.userId)
        val pends = groupUserRelationDao.listPending(managedGroupIds)
        return success(RelationUnionVO(follows, fans, groups, applications, pends))
    }

    override fun listMemberByGroup(groupId: Int, request: HttpServletRequest): Result<List<Int>?> {
        val token = request.getToken()
        if (!groupUserRelationDao.isMember(token.userId, groupId)) return error("权限错误")
        val relations = groupUserRelationDao.listMemberIdByTargetId(groupId)
        return success(relations)
    }

    override fun getLenOfMemberByGroup(groupId: Int, request: HttpServletRequest): Result<Int?> {
        val token = request.getToken()
        if (!groupUserRelationDao.isMember(token.userId, groupId)) return error("权限错误")
        val len = groupUserRelationDao.countMemberByTargetId(groupId)
        return success(len)
    }
}