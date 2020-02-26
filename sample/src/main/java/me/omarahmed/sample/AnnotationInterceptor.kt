package me.omarahmed.sample

import me.omarahmed.retrofitannotations.DefaultStore
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AnnotationInterceptor(private val store: DefaultStore<CustomAnnotation>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.d("intercepted request ${chain.request().url()}")
        val requestId = store.getRequestId(chain.request())
        Timber.d("its id = $requestId")
        val annotation = store.get(requestId)
        annotation?.let {
            Timber.d("annotation is $it")
            Timber.d("annotation message = ${it.message}")
        }
        return chain.proceed(chain.request())
    }

}