package com.zhou03.distribute.configuration

import com.zhou03.distribute.interceptor.LoginInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.ErrorPageRegistrar
import org.springframework.boot.web.server.ErrorPageRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File
import java.lang.System.getProperty


@Configuration
class MvcConfiguration : WebMvcConfigurer, ErrorPageRegistrar {


    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedOriginPatterns("*").allowedHeaders("*").allowedMethods("*")
            .allowCredentials(true).maxAge(3600)
    }

    override fun registerErrorPages(registry: ErrorPageRegistry) {
        val error404Page = ErrorPage(HttpStatus.NOT_FOUND, "/index.html")
        val error400Page = ErrorPage(HttpStatus.BAD_REQUEST, "/index.html")
        registry.addErrorPages(error404Page)
        registry.addErrorPages(error400Page)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        println("${File(getProperty("user.dir")).parent}/distribute-ui/dist")
        registry.addResourceHandler("/**").addResourceLocations("file:/www/wwwroot/phase/")
            .addResourceLocations("file:${File(getProperty("user.dir")).parent}/distribute-ui/build/web/")
    }

    @Autowired
    lateinit var loginInterceptor: LoginInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns(
            "/user/**", "/profile/**", "/device/**", "/message/**", "/relation/**", "/group/**"
        ).excludePathPatterns("/user/login", "/user/register", "/device/check", "/message/key/**")
    }
}