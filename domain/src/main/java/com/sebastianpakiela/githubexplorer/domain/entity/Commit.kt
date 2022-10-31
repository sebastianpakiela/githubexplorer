package com.sebastianpakiela.githubexplorer.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Commit(
    val date: String, val sha: String, val author: String, val message: String
) : Parcelable