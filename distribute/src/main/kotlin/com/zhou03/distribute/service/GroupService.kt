package com.zhou03.distribute.service

import com.zhou03.distribute.dao.GroupDao
import com.zhou03.distribute.dao.GroupUserRelationDao
import com.zhou03.distribute.domain.Group
import com.zhou03.distribute.domain.GroupRole
import com.zhou03.distribute.domain.GroupUserRelation
import com.zhou03.distribute.dto.group.*
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.util.upload
import com.zhou03.distribute.vo.GroupVO
import com.zhou03.distribute.vo.Result
import com.zhou03.distribute.vo.error
import com.zhou03.distribute.vo.success
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface GroupService {

    fun create(groupCreateDTO: GroupCreateDTO, request: HttpServletRequest): Result<GroupVO?>

    fun modify(groupModifyDTO: GroupModifyDTO, request: HttpServletRequest): Result<GroupVO?>

    fun delete(groupDeleteDTO: GroupDeleteDTO, request: HttpServletRequest): Result<Nothing?>

    fun invite(groupInviteDTO: GroupInviteDTO, request: HttpServletRequest): Result<String?>

    fun get(id: Int, request: HttpServletRequest): Result<GroupVO?>

    fun uploadAvatar(groupUploadAvatarDTO: GroupUploadAvatarDTO, request: HttpServletRequest): Result<String?>
}

@Service
class GroupServiceImpl : GroupService {

    @Autowired
    lateinit var groupDao: GroupDao

    @Autowired
    lateinit var groupUserRelationDao: GroupUserRelationDao

    @Value("\${file-save-path}")
    lateinit var fileSavePath: String

    override fun create(groupCreateDTO: GroupCreateDTO, request: HttpServletRequest): Result<GroupVO?> {
        val token = request.getToken()
        val group = Group().apply {
            this.title = groupCreateDTO.title
            this.createDate = LocalDateTime.now()
            this.visible = groupCreateDTO.visible
            this.status = true
        }
        groupDao.add(group)
        val relation = GroupUserRelation().apply {
            this.userId = token.userId
            this.targetId = group.id
            this.status = true
            this.path = "/"
            this.nickname = group.title
            this.role = GroupRole.MASTER
            this.date = LocalDateTime.now()
        }
        groupUserRelationDao.addOrUpdate(relation)
        return success(GroupVO.from(group))
    }

    override fun modify(groupModifyDTO: GroupModifyDTO, request: HttpServletRequest): Result<GroupVO?> {
        val token = request.getToken()
        if (!groupUserRelationDao.isManager(token.userId, groupModifyDTO.id)) return error("权限错误")
        val group = groupDao.getById(groupModifyDTO.id) ?: return error("权限错误")
        group.apply {
            this.title = groupModifyDTO.title
            this.visible = groupModifyDTO.visible
            flushChanges()
        }
        return success(GroupVO.from(group))
    }

    override fun delete(groupDeleteDTO: GroupDeleteDTO, request: HttpServletRequest): Result<Nothing?> {
        val token = request.getToken()
        if (groupDeleteDTO.userId == token.userId) return error("不能将所有权转给自己")
        if (!groupUserRelationDao.isMaster(token.userId, groupDeleteDTO.id)) return error("权限错误")
        val group = groupDao.getById(groupDeleteDTO.id) ?: return error("权限错误")
        val members = groupUserRelationDao.listMemberByTargetId(group.id)
        val member = members.find { groupDeleteDTO.userId == it.userId }
        if (member != null) {

            member.apply {
                this.role = GroupRole.MASTER
            }
            return success(message = "删除成功")
        }
        return error("删除失败")
    }

    override fun invite(groupInviteDTO: GroupInviteDTO, request: HttpServletRequest): Result<String?> {
        val token = request.getToken()
        if (!groupUserRelationDao.isManager(token.userId, groupInviteDTO.groupId)) return error("权限错误")
        groupDao.getById(groupInviteDTO.groupId) ?: return error("权限错误")
        return success("NaN")
    }

    override fun get(id: Int, request: HttpServletRequest): Result<GroupVO?> {
        val group = groupDao.getById(id) ?: return error("查无此项")
        return success(GroupVO.from(group))
    }

    override fun uploadAvatar(
        groupUploadAvatarDTO: GroupUploadAvatarDTO,
        request: HttpServletRequest,
    ): Result<String?> {
        val token = request.getToken()
        val filename = upload(fileSavePath, groupUploadAvatarDTO.file) ?: return error("更换失败")
        if (!groupUserRelationDao.isManager(token.userId, groupUploadAvatarDTO.id)) return error("权限错误")
        val group = groupDao.getById(groupUploadAvatarDTO.id) ?: return error("查无此项")
        if (group.avatarUrl !== "") com.zhou03.distribute.util.delete(fileSavePath, group.avatarUrl)
        group.apply {
            avatarUrl = filename
            flushChanges()
        }
        return success(filename)
    }
}