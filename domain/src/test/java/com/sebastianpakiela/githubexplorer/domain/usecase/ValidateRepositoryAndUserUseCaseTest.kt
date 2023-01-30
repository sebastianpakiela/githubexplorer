package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.rule.TestCoroutineRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ValidateRepositoryAndUserUseCaseTest(
    private val input: String,
    private val output: UserAndRepoValidationStatus
) {

    @get:Rule
    val testSchedulerRule = TestCoroutineRule()

    companion object {

        @Parameterized.Parameters
        @JvmStatic
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf("", UserAndRepoValidationStatus.EMPTY),
            arrayOf("            ", UserAndRepoValidationStatus.EMPTY),
            arrayOf("    google        ", UserAndRepoValidationStatus.NO_SLASH_PRESENT),
            arrayOf("google", UserAndRepoValidationStatus.NO_SLASH_PRESENT),
            arrayOf("google//", UserAndRepoValidationStatus.ONE_SLASH_ONLY_PERMITTED),
            arrayOf("google/", UserAndRepoValidationStatus.NO_REPO_NAME_FOUND),
            arrayOf("/dagger", UserAndRepoValidationStatus.NO_USER_NAME_FOUND),
            arrayOf("google/dagger", UserAndRepoValidationStatus.CORRECT)
        )
    }

    private val useCase: ValidateRepositoryAndUserUseCase = ValidateRepositoryAndUserUseCase()

    @Test
    fun `Should validate input`() {
//        val input = useCase.validateRepositoryAndUserInput(input).test()

//        assertThat(input.values().first(), equalTo(output))
    }
}