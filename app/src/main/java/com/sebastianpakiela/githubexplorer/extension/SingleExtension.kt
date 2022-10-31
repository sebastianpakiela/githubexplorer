package com.sebastianpakiela.githubexplorer.extension

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T> Single<T>.applyIoSchedulers(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}


fun <T> Single<T>.applyComputationsSchedulers(): Single<T> {
    return subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}


fun <T> Observable<T>.applyIoSchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}