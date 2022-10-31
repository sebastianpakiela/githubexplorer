package com.sebastianpakiela.githubexplorer.feature.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sebastianpakiela.githubexplorer.base.SingleLiveEvent
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.usecase.GetRecentlyViewedRepositoriesUseCase
import com.sebastianpakiela.githubexplorer.domain.usecase.GetRepositoryDataUseCase
import com.sebastianpakiela.githubexplorer.domain.usecase.UserAndRepoValidationStatus
import com.sebastianpakiela.githubexplorer.domain.usecase.ValidateRepositoryAndUserUseCase
import com.sebastianpakiela.githubexplorer.extension.applyIoSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import retrofit2.HttpException
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    getRecentlyViewedRepositoriesUseCase: GetRecentlyViewedRepositoriesUseCase,
    private val getRepositoryDataUseCase: GetRepositoryDataUseCase,
    private val validateRepositoryAndUserUseCase: ValidateRepositoryAndUserUseCase
) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    private val loadingLiveData = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = loadingLiveData

    private val errorLiveData = MutableLiveData<UserAndRepoValidationStatus>()
    val error: LiveData<UserAndRepoValidationStatus>
        get() = errorLiveData

    private val recentlyViewedRepositoriesLiveData = MutableLiveData<List<String>>()
    val recentlyViewedRepositories: LiveData<List<String>>
        get() = recentlyViewedRepositoriesLiveData

    val goToDetailsEvent = SingleLiveEvent<RepoCommitList>()
    val errorSnackBarEvent = SingleLiveEvent<Unit>()

    init {
        getRecentlyViewedRepositoriesUseCase
            .getRecentlyViewedRepositories()
            .applyIoSchedulers()
            .subscribeBy(
                onError = {},
                onNext = { recentlyViewedRepositoriesLiveData.value = it }
            )
            .addTo(disposable)
    }

    fun queryRepository(userAndRepoInput: String) {
        loadingLiveData.value = true
        val userAndRepoInput = userAndRepoInput.trim()

        validateRepositoryAndUserUseCase
            .validateRepositoryAndUserInput(userAndRepoInput)
            .doOnSuccess {
                errorLiveData.postValue(it)
            }
            .flatMap {
                when (it) {
                    UserAndRepoValidationStatus.CORRECT -> {
                        getRepositoryDataUseCase.getRepository(userAndRepoInput)
                    }
                    else -> {
                        Single.error(IllegalStateException("Validation error"))
                    }
                }
            }
            .applyIoSchedulers()
            .subscribeBy(
                onSuccess = {
                    loadingLiveData.value = false
                    goToDetailsEvent.value = it
                },
                onError = {
                    if (it is HttpException) {
                        errorSnackBarEvent.call()
                    }
                    loadingLiveData.value = false
                    Log.e("Error", it.message, it)
                }
            )
            .addTo(disposable)
    }

    fun clear() {
        disposable.clear()
    }
}