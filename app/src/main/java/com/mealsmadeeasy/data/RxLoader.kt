package com.mealsmadeeasy.data

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class RxLoader<T>(private val load: () -> Single<T>) {

    private val subject: BehaviorSubject<T> = BehaviorSubject.create()
    private val errorSubject: PublishSubject<Throwable> = PublishSubject.create()

    private val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    private var workerDisposable: Disposable? = null

    fun computeValue(): Observable<T> {
        isLoading.take(1).subscribe { loading ->
            if (!loading) {
                isLoading.onNext(true)
                workerDisposable = load().subscribe(this::setValue, this::onError)
            }
        }
        return getObservable()
    }

    fun getOrComputeValue(): Observable<T> {
        return if (!subject.hasValue()) computeValue()
        else getObservable()
    }

    fun isComputingValue(): Observable<Boolean> = isLoading

    fun getValue(): T? = if (subject.hasValue()) subject.value else null

    fun setValue(t: T) {
        workerDisposable?.dispose()
        workerDisposable = null

        subject.onNext(t)
        isLoading.onNext(false)
    }

    private fun onError(throwable: Throwable) {
        workerDisposable?.dispose()
        workerDisposable = null

        errorSubject.onNext(throwable)
        isLoading.onNext(false)
    }

    fun getObservable(): Observable<T> = subject.mergeWith(errorSubject.map { throw it })

    fun getError(): Observable<Throwable> = errorSubject

}