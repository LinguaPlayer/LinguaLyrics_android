package habibkazemi.ir.lingualyrics_android.vo

import habibkazemi.ir.lingualyrics_android.vo.Status.ERROR
import habibkazemi.ir.lingualyrics_android.vo.Status.LOADING
import habibkazemi.ir.lingualyrics_android.vo.Status.SUCCESS

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
