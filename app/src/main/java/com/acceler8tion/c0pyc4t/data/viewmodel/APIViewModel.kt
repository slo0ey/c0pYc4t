package com.acceler8tion.c0pyc4t.data.viewmodel

import androidx.lifecycle.ViewModel
import com.acceler8tion.c0pyc4t.data.api.GDApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class APIViewModel: ViewModel() {
    private val api = GDApi()

    suspend fun getLevel(id: String) = flow {
        emit(api.getLevel(id))
    }.flowOn(Dispatchers.IO)

    suspend fun uploadLevel(levelID: String, accountID: String, encryptedGjp: String, userName: String, levelData: Map<String, String>) = flow {
        emit(api.uploadLevel(levelID, accountID, encryptedGjp, userName, levelData))
    }.flowOn(Dispatchers.IO)

    suspend fun login(userName: String, password: String, udid: String) = flow {
        emit(api.login(userName, password, udid))
    }.flowOn(Dispatchers.IO)

}