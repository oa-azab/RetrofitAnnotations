package me.omarahmed.retrofitannotations

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type


class AnnotationsCallAdapterFactory<ID, T> internal constructor(
    private val store: Store<ID, T>,
    private val annotationClass: Class<T>
) :
    CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        val factories = retrofit.callAdapterFactories()
            .filterNot { it is AnnotationsCallAdapterFactory<*, *> }

        factories.forEach { factory ->
            val nullableAdapter = factory.get(returnType, annotations, retrofit)
            val annotation = getAnnotation(annotations)
            nullableAdapter?.let { adapter ->
                annotation?.let {
                    return WrappingCallAdapter(adapter, store, it)
                }
                return adapter
            }
        }
        return null
    }

    private fun getAnnotation(annotations: Array<Annotation>): T? {
        for (annotation in annotations) {
            if (annotationClass.name == annotation.annotationClass.qualifiedName) {
                return annotation as T
            }
        }
        return null
    }

}