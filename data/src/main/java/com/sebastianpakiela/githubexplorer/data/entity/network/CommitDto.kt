package com.sebastianpakiela.githubexplorer.data.entity.network

data class CommitDto(
    val author: AuthorDto,
    val message: String
)