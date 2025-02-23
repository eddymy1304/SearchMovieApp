package com.example.searchmovieapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "poster") val poster: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "year") val year: Int?,
    @ColumnInfo(name = "page") val page: Int? = null,
    @ColumnInfo(name = "isFavorite") val isFavorite: Int = 0
)