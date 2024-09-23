package com.theme.lambda.launcher.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theme.lambda.launcher.data.dao.ThemeResDao
import com.theme.lambda.launcher.data.model.ThemeRes

@Database(
    entities = [ThemeRes::class],
    version = 1,
    exportSchema = false
)
abstract class LauncherDataBase : RoomDatabase() {

    abstract fun themeResDao(): ThemeResDao


}