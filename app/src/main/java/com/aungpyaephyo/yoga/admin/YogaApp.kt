package com.aungpyaephyo.yoga.admin

import android.app.Application
import com.aungpyaephyo.yoga.admin.data.db.YogaDatabase
import com.aungpyaephyo.yoga.admin.data.repo.YogaRepository

import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class YogaApp : Application() {
    private val yogaDb by lazy { YogaDatabase.getInstance(this) }
    val repo by lazy {
        YogaRepository(
            yogaDao = yogaDb.yogaDao(),
        )
    }

    val syncDataUseCase by lazy { SyncDataUseCase(repo) }

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        coroutineScope.launch {
            syncDataUseCase.start()
        }
    }
}