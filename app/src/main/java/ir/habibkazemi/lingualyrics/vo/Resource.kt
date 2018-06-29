package ir.habibkazemi.lingualyrics.vo

import ir.habibkazemi.lingualyrics.vo.Status.ERROR
import ir.habibkazemi.lingualyrics.vo.Status.LOADING
import ir.habibkazemi.lingualyrics.vo.Status.SUCCESS

class Resource<T> private constructor(val status: Status, val data: T?, val message: String?) {
    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String?, data: T?): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}
