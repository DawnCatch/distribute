package com.zhou03.distribute.service

import com.zhou03.distribute.dao.ProfileDao
import com.zhou03.distribute.dto.profile.ProfileListByIdDTO
import com.zhou03.distribute.vo.ProfileVO
import com.zhou03.distribute.vo.Result
import com.zhou03.distribute.vo.error
import com.zhou03.distribute.vo.success
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ProfileService {

    fun getById(id: Int, request: HttpServletRequest): Result<ProfileVO?>

    fun listById(profileListByIdDTO: ProfileListByIdDTO, request: HttpServletRequest): Result<List<ProfileVO>?>
}

@Service
class ProfileServiceImpl : ProfileService {

    @Autowired
    lateinit var profileDao: ProfileDao

    override fun getById(id: Int, request: HttpServletRequest): Result<ProfileVO?> {
        val profile = profileDao.getById(id) ?: return error("查无此项")
        return success(ProfileVO.from(profile))
    }

    override fun listById(profileListByIdDTO: ProfileListByIdDTO, request: HttpServletRequest): Result<List<ProfileVO>?> {
        val profiles = profileDao.listById(profileListByIdDTO.ids)
        return success(profiles.map { ProfileVO.from(it) })
    }
}