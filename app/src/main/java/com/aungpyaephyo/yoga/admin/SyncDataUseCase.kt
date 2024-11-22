package com.aungpyaephyo.yoga.admin
import com.aungpyaephyo.yoga.admin.data.model.YogaCourse
import com.aungpyaephyo.yoga.admin.data.repo.YogaRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await

class SyncDataUseCase(
    private val repo: YogaRepository
) {
    private val collection = Firebase.firestore.collection("yoga_courses")

    suspend fun start() {
        repo.getAllCourses()
            .onEach { uploadToFireStore(it, false) }
            .collect()
    }

    suspend fun uploadToFireStore(
        courses: List<YogaCourse>,
        reset: Boolean
    ): Result<Unit> {
        return try {
            if (reset) {
                val result = collection.get().await()
                result.documents.forEach { doc ->
                    doc.reference.delete()
                }
            }
            courses.forEach {yogaCourse ->


                collection.document(yogaCourse.id)
                    .set(yogaCourse)
                    .await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}