package com.aungpyaephyo.yoga.admin.data.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [
        YogaCourseEntity::class,
//        YogaImageEntity::class,
        YogaClassEntity::class,
        YogaTeacherEntity::class,
        YogaClassTeacherCrossRef::class
    ],
    version = 1
)
abstract class YogaDatabase : RoomDatabase() {
    abstract fun yogaDao(): YogaDao

    companion object {
        private const val DATABASE_NAME = "yoga_db"

        fun getInstance(applicationContext: Context): YogaDatabase {
            return Room.databaseBuilder(
                applicationContext,
                YogaDatabase::class.java, DATABASE_NAME
            ).build()
        }
    }

}