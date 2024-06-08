package com.zhou03.distribute.service

import com.zhou03.distribute.dao.GroupDao
import com.zhou03.distribute.dao.GroupUserProfileDao
import com.zhou03.distribute.dao.RelationDao
import com.zhou03.distribute.domain.Group
import com.zhou03.distribute.domain.Relation
import com.zhou03.distribute.dto.group.GroupCreateDTO
import com.zhou03.distribute.dto.group.GroupDeleteDTO
import com.zhou03.distribute.dto.group.GroupInviteDTO
import com.zhou03.distribute.dto.group.GroupModifyDTO
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.vo.GroupVO
import com.zhou03.distribute.vo.Result
import com.zhou03.distribute.vo.error
import com.zhou03.distribute.vo.success
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface GroupService {

    fun create(groupCreateDTO: GroupCreateDTO, request: HttpServletRequest): Result<GroupVO?>

    fun modify(groupModifyDTO: GroupModifyDTO, request: HttpServletRequest): Result<GroupVO?>

    fun delete(groupDeleteDTO: GroupDeleteDTO, request: HttpServletRequest): Result<Nothing?>

    fun invite(groupInviteDTO: GroupInviteDTO, request: HttpServletRequest): Result<String?>

    fun get(id: Int, request: HttpServletRequest): Result<GroupVO?>
}

@Service
class GroupServiceImpl : GroupService {

    @Autowired
    lateinit var groupDao: GroupDao

    @Autowired
    lateinit var relationDao: RelationDao

    @Autowired
    lateinit var groupUserProfileDao: GroupUserProfileDao

    override fun create(groupCreateDTO: GroupCreateDTO, request: HttpServletRequest): Result<GroupVO?> {
        val token = request.getToken()
        val group = Group().apply {
            this.title = groupCreateDTO.title
            this.creatorId = token.userId
            this.createDate = LocalDateTime.now()
        }
        groupDao.add(group)
        val relation = Relation().apply {
            this.userId = token.userId
            this.targetId = group.id
            this.status = true
            this.date = LocalDateTime.now()
            this.path = "/"
        }
        relationDao.add(relation)
        return success(GroupVO.from(group))
    }

    override fun modify(groupModifyDTO: GroupModifyDTO, request: HttpServletRequest): Result<GroupVO?> {
        val token = request.getToken()
        val group = groupDao.getByIdAsOwn(groupModifyDTO.id, token.userId) ?: return error("权限错误")
        group.apply {
            this.title = groupModifyDTO.title
            flushChanges()
        }
        return success(GroupVO.from(group))
    }

    override fun delete(groupDeleteDTO: GroupDeleteDTO, request: HttpServletRequest): Result<Nothing?> {
        val token = request.getToken()
        val group = groupDao.getByIdAsOwn(groupDeleteDTO.id, token.userId) ?: return error("权限错误")
        group.delete()
        return success(message = "删除成功")
    }

    override fun invite(groupInviteDTO: GroupInviteDTO, request: HttpServletRequest): Result<String?> {
        val token = request.getToken()
        val group = groupDao.getByIdAsOwn(groupInviteDTO.groupId, token.userId) ?: return error("权限错误")
        return success("NaN")
    }

    override fun get(id: Int, request: HttpServletRequest): Result<GroupVO?> {
        val token = request.getToken()
        val group = groupDao.getById(id) ?: return error("查无此项")
        if (relationDao.isMemberOfGroup(token.userId, group.id)) {
            val profiles = groupUserProfileDao.listByGroupId(group.id)
            return success(GroupVO.from(group, profiles))
        } else return success(GroupVO.from(group))
    }
}