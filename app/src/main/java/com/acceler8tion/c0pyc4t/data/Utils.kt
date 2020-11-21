package com.acceler8tion.c0pyc4t.data

import org.apache.commons.codec.binary.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.experimental.xor


class Utils {
    companion object {
        private val CHARTABLE= "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toList()
        private const val HEX_CHARS = "0123456789ABCDEF"

        fun parseToMap(str: String, regex: String): Map<String, String> {
            val list = str.split(regex)
            val m = mutableMapOf<String, String>()
            for(i in list.indices step 2) {
                m[list[i]] = list[i + 1]
            }
            return m
        }

        fun generateParam(param: Map<String, String>): String {
            return param.map {
                return@map "${it.key}=${it.value}"
            }.joinToString("&")
        }

        fun <T> getOrElse(obj: T?, replacer: T): T{
            return obj?:replacer
        }

        fun rs(): String {
            return List<Char>(10) {
                CHARTABLE.random()
            }.joinToString { "" }
        }

        fun seed(data: String): String {
            return if(data.length < 50) data
            else {
                val spc: Int = data.length / 50
                data.foldIndexed("") { idx, acc, v -> if(idx % spc == 0) acc+v else acc}.slice(0 until 50)
            }
        }

        fun chk(value: String): String? {
            val salt = "xI25fpAapCQg"
            val sha = sha1(value.plus(salt))
            val xorstr = xor(sha.toLowerCase(Locale.getDefault()))
            return Base64.encodeBase64String(xorstr.toByteArray()) ?: null
        }

        private fun sha1(input: String): String {
            val bytes = MessageDigest
                    .getInstance("SHA-1")
                    .digest(input.toByteArray())
            val result = StringBuilder(bytes.size * 2)

            bytes.forEach {
                val i = it.toInt()
                result.append(HEX_CHARS[i shr 4 and 0x0f])
                result.append(HEX_CHARS[i and 0x0f])
            }

            return result.toString()
        }

        private fun xor(input: String): String {
            val key = "41274".toByteArray() //Can be any chars, and any length array
            val output = StringBuilder()

            for (i in input.indices) {
                val a: Int = input[i].toInt()
                val b: Int = key[i % key.size].toInt()
                output.append((a xor b).toChar())
            }

            return output.toString()
        }
    }
}