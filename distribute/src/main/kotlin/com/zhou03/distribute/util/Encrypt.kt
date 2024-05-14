package com.zhou03.distribute.util

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun String.aesEncrypt(key: String = "2024050820240508"): String {
    val cipher = Cipher.getInstance("AES")
    val keySpec = SecretKeySpec(key.toByteArray(), "AES")
    cipher.init(Cipher.ENCRYPT_MODE, keySpec)
    val encrypt = cipher.doFinal(this.toByteArray())
    return Base64.encode(encrypt)
}

@OptIn(ExperimentalEncodingApi::class)
fun String.aesDecrypt(key: String = "2024050820240508"): String {
    val cipher = Cipher.getInstance("AES")
    val keySpec = SecretKeySpec(key.toByteArray(), "AES")
    cipher.init(Cipher.DECRYPT_MODE, keySpec)
    val decrypt = cipher.doFinal(Base64.decode(this))
    return String(decrypt)
}

fun String.toMd5() = md5(this)

fun md5(content: String): String {
    val hash = MessageDigest.getInstance("MD5").digest(content.toByteArray())
    val hex = StringBuilder(hash.size * 2)
    for (b in hash) {
        var str = Integer.toHexString(b.toInt())
        if (b < 0x10) {
            str = "0$str"
        }
        hex.append(str.substring(str.length - 2))
    }
    return hex.toString()
}