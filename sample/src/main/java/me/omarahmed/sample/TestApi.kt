package me.omarahmed.sample

import com.squareup.moshi.Json
import io.reactivex.Single
import me.omarahmed.retrofitannotations.DefaultStore
import me.omarahmed.retrofitannotations.RetrofitAnnotations
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import timber.log.Timber


class TestApi {

    companion object {

        private const val BASE_URL = "http://worldtimeapi.org/"
        private var service: TestService? = null
        private val store = DefaultStore<CustomAnnotation>()
        private val lock = Any()

        fun getService(): TestService {
            synchronized(lock) {
                if (service == null) {
                    val httpClient = OkHttpClient.Builder()
                        .addInterceptor(AnnotationInterceptor(store))
                        .build()

                    val retrofit = RetrofitAnnotations.builder(store, CustomAnnotation::class.java)
                        .baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(MoshiConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(httpClient)
                        .build()

                    Timber.d("Factories = ${retrofit.callAdapterFactories()}")
                    Timber.d("Factories size = ${retrofit.callAdapterFactories().size}")

                    service = retrofit.create(TestService::class.java)
                }
                return service!!
            }
        }
    }

    interface TestService {

        @CustomAnnotation("custom message")
        @GET("api/ip")
        fun getTime(): Single<Response<TimeResponse>>

    }

    data class TimeResponse(@field:Json(name = "unixtime") val unixTime: Long)
}