package com.zhou03.distribute.vo

data class Result<T>(
    val status: Boolean,
    var message: String,
    val data: T,
)
fun <T> success(data: T? = null, message: String = "success") = Result(true, message, data)

fun <T> error(message: String, data: T? = null): Result<T?> = Result(false, message, data)

infix fun <T> Result<T>.send(message: String): Result<T> = this.apply {
    this.message = message
}