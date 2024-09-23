package com.theme.lambda.launcher.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "theme_res",
    indices = [androidx.room.Index(value = ["did"], unique = true)]
)
class ThemeRes {
    @PrimaryKey
    @ColumnInfo("did")
    var did: String = ""

    @ColumnInfo("access_type")
    var accessType: Int = 0

    @ColumnInfo("preview_url")
    var previewUrl: String = ""

    @ColumnInfo("zip_url")
    var zipUrl: String = ""

    @ColumnInfo("download_date")
    var downloadDate: Long = 0
}