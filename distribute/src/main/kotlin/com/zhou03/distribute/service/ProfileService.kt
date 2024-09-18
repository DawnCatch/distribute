package com.zhou03.distribute.service

import com.zhou03.distribute.dao.ProfileDao
import com.zhou03.distribute.dto.profile.ProfileListByIdDTO
import com.zhou03.distribute.util.delete
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.util.upload
import com.zhou03.distribute.vo.ProfileVO
import com.zhou03.distribute.vo.Result
import com.zhou03.distribute.vo.error
import com.zhou03.distribute.vo.success
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

interface ProfileService {

    fun getById(id: Int, request: HttpServletRequest): Result<ProfileVO?>

    fun listById(profileListByIdDTO: ProfileListByIdDTO, request: HttpServletRequest): Result<List<ProfileVO>?>

    fun uploadAvatar(file: MultipartFile, request: HttpServletRequest): Result<String?>
}

@Service
class ProfileServiceImpl : ProfileService {

    @Autowired
    lateinit var profileDao: ProfileDao

    @Value("\${file-save-path}")
    lateinit var fileSavePath: String

    override fun getById(id: Int, request: HttpServletRequest): Result<ProfileVO?> {
        val profile = profileDao.getById(id) ?: return error("查无此项")
        return success(ProfileVO.from(profile))
    }

    override fun listById(
        profileListByIdDTO: ProfileListByIdDTO,
        request: HttpServletRequest,
    ): Result<List<ProfileVO>?> {
        val profiles = profileDao.listById(profileListByIdDTO.ids)
        return success(profiles.map { ProfileVO.from(it) })
    }

    override fun uploadAvatar(file: MultipartFile, request: HttpServletRequest): Result<String?> {
        val token = request.getToken()
        val filename = upload(fileSavePath, file) ?: return error("更换失败")
        val profile = profileDao.getById(token.userId) ?: return error("查无此项")
        if (profile.avatarUrl !== "") delete(fileSavePath, profile.avatarUrl)
        profile.apply {
            avatarUrl = filename
            flushChanges()
        }
        return success(filename)
    }
}