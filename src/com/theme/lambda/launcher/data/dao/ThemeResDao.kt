package com.theme.lambda.launcher.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.theme.lambda.launcher.data.model.ThemeRes

@Dao
interface ThemeResDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: ThemeRes): Long

    @Query("SELECT * FROM theme_res order by download_date DESC limit 100")
    fun getDownLoadThemeResList(): List<ThemeRes>
}