package com.acceler8tion.c0pyc4t.data.api

import android.util.Base64
import com.acceler8tion.c0pyc4t.data.Utils
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException

class GDApi {
    companion object {
        private const val baseUrl = "http://www.boomlings.com/database"
        private fun baseParam(): MutableMap<String, String> = mutableMapOf (
            "gameVersion" to "21",
            "binaryVersion" to "35",
            "gdw" to "0",
            "secret" to "Wmfd2893gb7"
        )
    }

    fun getLevel(id: String): String? {
        val param = baseParam()
        param["levelID"] = id
        param["inc"] = "0"
        param["extras"] = "1"
        param["secret"] = "Wmfd2893gb7"

        return try {
            val result = Jsoup.connect("$baseUrl/downloadGJLevel22.php")
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .maxBodySize(0)
                .requestBody(Utils.generateParam(param.toMap()))
                .execute().body()
            println(result)
            if(result == "-1") null else result
        } catch (e: IOException) {
            println(e.message!!)
            null
        }
    }

    fun uploadLevel(levelID: String, accountID: String, encryptedGjp: String, userName: String, levelData: Map<String, String>): String? {
        val param = baseParam()
        val levelName = "${Utils.getOrElse(levelData["2"], levelID)} copy"
        val data = Utils.getOrElse(levelData["4"], "")
        val seed2 = Utils.seed(data)
        val chk = Utils.chk(seed2) ?: return null
        param["accountID"] = accountID
        param["gjp"] = encryptedGjp
        param["userName"] = userName
        param["levelID"] = "0"
        param["levelName"] = if(levelName.length > 20) {
            "$levelID copy"
        } else {
            levelName
        }
        param["levelDesc"] = Base64.encodeToString("clone by c0pYc4t".toByteArray(), Base64.DEFAULT)
        param["levelVersion"] = "1"
        param["levelLength"] = Utils.getOrElse(levelData["15"], "0")
        param["audioTrack"] = Utils.getOrElse(levelData["12"], "0")
        param["auto"] = Utils.getOrElse(levelData["25"], "0")
        param["password"] = "1"
        param["original"] = "0"
        param["twoPlayer"] = Utils.getOrElse(levelData["31"], "0")
        param["songID"] = Utils.getOrElse(levelData["35"], "0")
        param["objects"] = Utils.getOrElse(levelData["45"], "0")
        param["coins"] = Utils.getOrElse(levelData["37"], "0")
        param["requestStars"] = "0"
        param["unlisted"] = "0"
        param["wt"] = Utils.getOrElse(levelData["46"], "0")
        param["wt2"] = Utils.getOrElse(levelData["47"], "0")
        param["ldm"] = Utils.getOrElse(levelData["40"], "0")
        param["extraString"] = Utils.getOrElse(levelData["36"],
                "0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0_0"
        )
        param["seed"] = Utils.rs()
        param["seed2"] = chk
        param["levelString"] = data
        param["levelInfo"] = Utils.getOrElse(levelData["26"], "") //pls never

        return try {
            val result = Jsoup.connect("$baseUrl/uploadGJLevel21.php")
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .requestBody(Utils.generateParam(param.toMap()))
                    .execute().body()
            println(result)
            if(result == "-1") null else levelName
        } catch (e: IOException) {
            null
        }
    }

    fun login(userName: String, password: String, udid: String): String? {
        val param = baseParam()
        param["userName"] = userName
        param["password"] = password
        param["udid"] = udid
        param["secret"] = "Wmfv3899gc9"

        return try {
            val result = Jsoup.connect("$baseUrl/accounts/loginGJAccount.php")
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .maxBodySize(0)
                .requestBody(Utils.generateParam(param.toMap()))
                .execute().body()
            println("Result: $result")
            if(result == "-1" || result == "-2") null else result
        } catch (e: IOException) {
            null
        }
    }
}