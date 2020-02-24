package me.omarahmed.retrofitannotations

import android.util.SparseArray
import okhttp3.Request

class DefaultStore<T> : Store<Int, T> {

    companion object {
        private const val HASH_PRIME = 31
    }

    private val store = SparseArray<T>()

    override fun get(key: Int): T? {
        return store[key]
    }

    override fun put(key: Int, value: T) {
        store.put(key, value)
    }

    /**
     * Creates an identifier, which can identify the request by itself
     *
     * @param request request to create the identifier for
     * @return an identifier
     */
    override fun getRequestId(request: Request): Int =
        request.url().hashCode() + HASH_PRIME * request.method().hashCode()

}