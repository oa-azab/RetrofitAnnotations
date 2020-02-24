package me.omarahmed.retrofitannotations

import android.util.Log
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class WrappingCallAdapter<Any, RETURN_TYPE, ID, T>(
    private val adapter: CallAdapter<Any, RETURN_TYPE>,
    private val store: Store<ID, T>,
    private val annotation: T
) :
    CallAdapter<Any, RETURN_TYPE> {

    override fun adapt(call: Call<Any>): RETURN_TYPE {
        val requestId = store.getRequestId(call.request())

        Log.d("adapt", "[adapt] requestId = $requestId, and authLevel = $annotation")
        store.put(requestId, annotation)

        return adapter.adapt(call)
    }

    override fun responseType(): Type {
        return adapter.responseType()
    }
}