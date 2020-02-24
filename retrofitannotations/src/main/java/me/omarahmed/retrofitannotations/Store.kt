package me.omarahmed.retrofitannotations

import okhttp3.Request

interface Store<KEY, VALUE> {

    fun get(key: KEY): VALUE?

    fun put(key: KEY, value: VALUE)

    fun getRequestId(request: Request): KEY

}