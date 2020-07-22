package com.achesnovitskiy.octocattest2.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Repo(
    @PrimaryKey val id: Int,
    val name: String
)