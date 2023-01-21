package com.sebastianpakiela.githubexplorer.data.api

import com.sebastianpakiela.githubexplorer.data.entity.network.CommitEntryDto
import com.sebastianpakiela.githubexplorer.data.entity.network.RepositoryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApiService {

    @GET("/repos/{userAndRepo}/commits?per_page=100&page=1")
    suspend fun getCommitData(
        @Path(value = "userAndRepo", encoded = true) userAndRepo: String
    ): List<CommitEntryDto>

    @GET("/repos/{userAndRepo}")
    suspend fun getRepositoryData(
        @Path(value = "userAndRepo", encoded = true) userAndRepo: String
    ): RepositoryDto
}