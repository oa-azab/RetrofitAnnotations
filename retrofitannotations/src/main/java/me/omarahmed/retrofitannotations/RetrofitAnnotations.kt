package me.omarahmed.retrofitannotations

import retrofit2.Retrofit

object RetrofitAnnotations {

    @JvmStatic
    fun <ID, T> builder(store: Store<ID, T>, annotationClass: Class<T>): Retrofit.Builder {
        val adapter = AnnotationsCallAdapterFactory(store, annotationClass)
        return Retrofit.Builder()
            .addCallAdapterFactory(adapter)
    }

}