package com.zhou03.distribute.service

import com.zhou03.distribute.dao.ProfileDao
import com.zhou03.distribute.dao.UserDao
import com.zhou03.distribute.domain.Profile
import com.zhou03.distribute.domain.User
import com.zhou03.distribute.dto.user.UserLoginDTO
import com.zhou03.distribute.dto.user.UserRegisterDTO
import com.zhou03.distribute.dto.user.UserSetEmailDTO
import com.zhou03.distribute.model.Email
import com.zhou03.distribute.model.Token
import com.zhou03.distribute.util.TokenUtil
import com.zhou03.distribute.util.TokenUtil.SUBJECT
import com.zhou03.distribute.util.getAndTrans
import com.zhou03.distribute.util.getToken
import com.zhou03.distribute.util.toMd5
import com.zhou03.distribute.vo.ProfileVO
import com.zhou03.distribute.vo.Result
import com.zhou03.distribute.vo.error
import com.zhou03.distribute.vo.success
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface UserService {

    fun login(userLoginDTO: UserLoginDTO, response: HttpServletResponse): Result<ProfileVO?>

    fun register(userRegisterDTO: UserRegisterDTO, response: HttpServletResponse): Result<ProfileVO?>

    fun reconnect(request: HttpServletRequest): Result<ProfileVO?>

    fun setEmail(userSetEmailDTO: UserSetEmailDTO, request: HttpServletRequest): Result<Nothing?>

    fun confirm(code: String): Result<Nothing?>
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
        response.setHeader(SUBJECT, TokenUtil.genAccessToken(Token.from(user)))
        return success(ProfileVO.from(profile))
    }

    override fun register(userRegisterDTO: UserRegisterDTO, response: HttpServletResponse): Result<ProfileVO?> {
        if (userDao.countUsernameByValue(userRegisterDTO.username) > 0) return error("用户名已被使用")
        val user = User().apply {
            this.username = userRegisterDTO.username
            this.password = userRegisterDTO.password.toMd5()
        }
        userDao.add(user)
        val profile = Profile().apply {
            this.id = user.id
            this.nickname = user.username
        }
        profileDao.add(profile)
        response.setHeader(SUBJECT, TokenUtil.genAccessToken(Token.from(user)))
        return success(ProfileVO.from(profile))
    }

    override fun reconnect(request: HttpServletRequest): Result<ProfileVO?> {
        val token = request.getToken()
        val profile = profileDao.getById(token.userId) ?: return error("访问错误")
        return success(ProfileVO.from(profile))
    }

    override fun setEmail(userSetEmailDTO: UserSetEmailDTO, request: HttpServletRequest): Result<Nothing?> {
        val token = request.getToken()
        if (!userDao.emailIsEmpty(token.userId)) return error("已设置邮箱")
        if (userDao.countEmailByValue(userSetEmailDTO.email) > 0) return error("邮箱已被使用")
        TokenUtil.genAccessToken("email", Email(token.userId, userSetEmailDTO.email))/*
            发送验证链接到指定邮箱
         */
        return success()
    }

    override fun confirm(code: String): Result<Nothing?> {
        val claims: Claims
        try {
            claims = TokenUtil.parsePayload(code)
        } catch (_: SignatureException) {
            //密钥被篡改
            return error("验证失败")
        } catch (_: ExpiredJwtException) {
            //密钥过期
            return error("链接已过期")
        }
        val email = claims.getAndTrans<Email>("email") ?: return error("验证失败")
        userDao.setEmail(email.userId, email.email)
        return success(null, "验证成功")
    }
}