package habibkazemi.ir.lingualyrics_android.repository


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.os.AsyncTask
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread

import habibkazemi.ir.lingualyrics_android.vo.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread internal constructor() {
    private val result = MediatorLiveData<Resource<ResultType>>()

    val asLiveData: LiveData<Resource<ResultType>>
        get() = result

    init {
        result.value = Resource.loading(null)
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    result.setValue(Resource.success(newData)) }
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        // TODO: update this function with executers :
        // https://github.com/googlesamples/android-architecture-components/blob/d7cd7a9414ef1eeba52a79ec76f6d9827cd50a36/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.kt
        result.addSource(dbSource) { newData -> result.setValue(Resource.loading(newData)) }
        createCall().enqueue(object : Callback<RequestType> {
            override fun onResponse(call: Call<RequestType>, response: Response<RequestType>) {
                result.removeSource(dbSource)
                saveResultAndReInit(response.body())
            }

            override fun onFailure(call: Call<RequestType>, t: Throwable) {
                onFetchFailed()
                result.removeSource(dbSource)
                result.addSource(dbSource) { newData -> result.setValue(Resource.error(t.message, newData)) }
            }
        })
    }

    @MainThread
    private fun saveResultAndReInit(response: RequestType?) {
        object : AsyncTask<Void, Void, Void>() {

            override fun doInBackground(vararg args: Void?): Void? {
                saveCallResult(response)
                return null
            }

            override fun onPostExecute(arg: Void?) {
                // I specially request a new live data,
                // if dbSource has already emitted a value, result.addSource(dbSource) will emit cache value. result.addSource(loadFromDb()) will emit newly Value.
                result.addSource(loadFromDb()) { newData -> result.setValue(Resource.success(newData)) }
            }
        }.execute()
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType?)

    @MainThread
    protected open fun shouldFetch(data: ResultType?): Boolean {
        return true
    }

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): Call<RequestType>

    @MainThread
    protected fun onFetchFailed() {
    }
}


