package me.omarahmed.sample

import com.google.gson.GsonBuilder
import io.reactivex.Single
import me.omarahmed.retrofitannotations.AnnotationsCallAdapterFactory
import me.omarahmed.retrofitannotations.DefaultStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import timber.log.Timber


class TestApi {

    companion object {

        private var service: TestService? = null
        private val store = DefaultStore<AuthLevel>()
        private val lock = Any()

        fun getService(): TestService {
            synchronized(lock) {
                if (service == null) {
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()

                    val httpClient = OkHttpClient.Builder()
                        .addInterceptor(AuthInterceptor(store))
                        .build()

                    val retrofit = AnnotationsCallAdapterFactory
                        .getRetrofitBuilder(store, AuthLevel::class.java)
                        .baseUrl("https://testnumberone.free.beeceptor.com")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
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

        @AuthLevel(1)
        @GET("my/api/path")
        fun getText(): Call<String>

        @AuthLevel(1)
        @GET("my/api/path")
        fun getText2(): Single<String>

    }

    class AuthInterceptor(private val store: DefaultStore<AuthLevel>) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            Timber.d("[intercept] intercepted request ${chain.request().url()}")
            val requestId = store.getRequestId(chain.request())
            Timber.d("[intercept] its id = $requestId")
            val authLevel = store.get(requestId)
            authLevel?.let {
                Timber.d("[intercept] authLevel is $it")
            }
            return chain.proceed(chain.request())
        }

    }
}