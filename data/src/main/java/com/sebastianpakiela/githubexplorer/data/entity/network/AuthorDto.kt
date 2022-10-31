package com.sebastianpakiela.githubexplorer.data.entity.network

import java.time.Instant

data class AuthorDto(
    val name: String,
    val date: Instant
)