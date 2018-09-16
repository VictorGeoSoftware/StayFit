package com.victor.health.stayfit.presenter

/**
 * Created by victorpalmacarrasco on 13/9/18.
 * ${APP_NAME}
 */
abstract class ParentPresenter<T1> {
    var view: T1? = null

    open fun destroy() { }
}