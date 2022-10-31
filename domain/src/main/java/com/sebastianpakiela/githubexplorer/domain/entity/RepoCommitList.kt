package com.sebastianpakiela.githubexplorer.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepoCommitList(
    val list: List<Commit>
) : Parcelable
