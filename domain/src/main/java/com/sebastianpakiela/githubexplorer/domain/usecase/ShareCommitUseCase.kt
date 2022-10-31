package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.entity.Commit
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CommitToShareTextUseCase @Inject constructor() {

    fun execute(commit: Commit): Single<String> {
        return Single.just(
            """Comiit: ${commit.message} with sha:${commit.sha} was created by ${commit.author} on ${commit.date}"""
        )
    }
}