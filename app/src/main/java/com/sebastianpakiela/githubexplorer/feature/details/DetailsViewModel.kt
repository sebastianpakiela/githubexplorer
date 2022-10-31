package com.sebastianpakiela.githubexplorer.feature.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sebastianpakiela.githubexplorer.base.SingleLiveEvent
import com.sebastianpakiela.githubexplorer.domain.entity.Commit
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.usecase.CommitToShareTextUseCase
import com.sebastianpakiela.githubexplorer.extension.applyComputationsSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val commitToShareTextUseCase: CommitToShareTextUseCase
) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    private val commitListLiveData = MutableLiveData<List<Commit>>()
    val commitList: LiveData<List<Commit>>
        get() = commitListLiveData

    val shareCommitEvent = SingleLiveEvent<String>()

    fun initFrom(repoCommitList: RepoCommitList) {
        commitListLiveData.value = repoCommitList.list
    }

    fun onShareClick(commit: Commit) {
        commitToShareTextUseCase
            .execute(commit)
            .applyComputationsSchedulers()
            .subscribeBy(
                onError = { Log.e("Error", "Error") },
                onSuccess = { shareCommitEvent.value = it }
            )
            .addTo(disposable)
    }

    fun clear() {
        disposable.clear()
    }
}