package com.zhou03.distribute.service

import com.zhou03.distribute.dao.ProfileDao
import com.zhou03.distribute.dao.UserDao
import com.zhou03.distribute.domain.Profile
import com.zhou03.distribute.domain.User
import com.zhou03.distribute.dto.UserLoginDTO
import com.zhou03.distribute.dto.UserRegisterDTO
import com.zhou03.distribute.util.cookie
import com.zhou03.distribute.util.getUser
import com.zhou03.distribute.util.toMd5
import com.zhou03.distribute.util.token
import com.zhou03.distribute.vo.ProfileVO
import com.zhou03.distribute.vo.Result
import com.zhou03.distribute.vo.error
import com.zhou03.distribute.vo.success
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface UserService {

    fun login(userLoginDTO: UserLoginDTO, response: HttpServletResponse): Result<ProfileVO?>

    fun register(userRegisterDTO: UserRegisterDTO, response: HttpServletResponse): Result<ProfileVO?>

    fun reconnect(request: HttpServletRequest): Result<ProfileVO?>
}

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userDao: UserDao

    @Autowired
    lateinit var profileDao: ProfileDao

    override fun login(userLoginDTO: UserLoginDTO, response: HttpServletResponse): Result<ProfileVO?> {
        val user = userDao.check(userLoginDTO.username, userLoginDTO.password.toMd5()) ?: return error("账号或密码错误")
        val profile = profileDao.getById(user.id) ?: Profile().apply {
            nickname = user.username
        }
        response.addCookie(token(user))
        return success(ProfileVO.from(profile))
    }

    override fun register(userRegisterDTO: UserRegisterDTO, response: HttpServletResponse): Result<ProfileVO?> {
        if (userDao.countUsernameByValue(userRegisterDTO.username) > 0) return error("用户名已被使用")
        else if (userDao.countUsernameByValue(userRegisterDTO.username) > 0) return error("邮箱已被使用")
        val user = User().apply {
            this.username = userRegisterDTO.username
            this.email = userRegisterDTO.email
            this.password = userRegisterDTO.password.toMd5()
        }
        userDao.add(user)
        val profile = Profile().apply {
            this.id = user.id
            this.nickname = user.username
        }
        profileDao.add(profile)
        response.addCookie(token(user))
        return success(ProfileVO.from(profile))
    }

    override fun reconnect(request: HttpServletRequest): Result<ProfileVO?> {
        val user = request.getUser()
        val profile = profileDao.getById(user.id) ?: Profile().apply {
            nickname = user.username
        }
        return success(ProfileVO.from(profile))
    }

}