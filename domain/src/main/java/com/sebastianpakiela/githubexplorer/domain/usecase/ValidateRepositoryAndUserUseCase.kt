package com.sebastianpakiela.githubexplorer.domain.usecase

import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ValidateRepositoryAndUserUseCase @Inject constructor() {

    fun validateRepositoryAndUserInput(userAndRepo: String): Single<UserAndRepoValidationStatus> {
        return Single
            .just(userAndRepo)
            .map { input ->
                when {
                    input.isNullOrBlank() -> UserAndRepoValidationStatus.EMPTY
                    input.contains("/").not() -> UserAndRepoValidationStatus.NO_SLASH_PRESENT
                    input.count { it == '/' } > 1 -> UserAndRepoValidationStatus.ONE_SLASH_ONLY_PERMITTED
                    input.substringBefore('/', "").isEmpty() ->
                        UserAndRepoValidationStatus.NO_USER_NAME_FOUND
                    input.substringAfter('/', "").isEmpty() ->
                        UserAndRepoValidationStatus.NO_REPO_NAME_FOUND
                    else -> UserAndRepoValidationStatus.CORRECT
                }
            }
    }

}

enum class UserAndRepoValidationStatus {
    EMPTY,
    NO_SLASH_PRESENT,
    ONE_SLASH_ONLY_PERMITTED,
    NO_USER_NAME_FOUND,
    NO_REPO_NAME_FOUND,
    CORRECT;
}