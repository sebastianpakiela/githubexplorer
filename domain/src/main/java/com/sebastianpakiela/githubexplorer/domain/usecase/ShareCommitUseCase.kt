package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.entity.Commit
import javax.inject.Inject

class CommitToShareTextUseCase @Inject constructor() {

    fun execute(commit: Commit): String {
        return """Comiit: ${commit.message} with sha:${commit.sha} was created by ${commit.author} on ${commit.date}"""
    }
}