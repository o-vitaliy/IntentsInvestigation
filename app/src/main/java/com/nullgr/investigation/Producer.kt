package com.nullgr.investigation

import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 * @author ovitaliy
 */

object Producer {

    fun createObserver(countInPack: Int, interval: Long)
            = Observable.interval(interval, TimeUnit.MILLISECONDS)
            .map { IntentFactory.createIntents(countInPack) }
            .flatMap { Observable.fromIterable(it) }



}
