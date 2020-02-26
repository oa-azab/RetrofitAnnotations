package me.omarahmed.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private val service = TestApi.getService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        btnGetTime.setOnClickListener { getTime() }
    }

    private fun getTime() {
        updateUi("Loading ...")
        service.getTime()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d(it.toString())
                val body = it.body()
                if (it.isSuccessful && body != null) {
                    val date = Date(body.unixTime.times(1000))
                    updateUi(date.toString())
                } else {
                    updateUi(it.toString())
                }
            }, {
                Timber.e(it)
                updateUi(it.message ?: "Unknown Error")
            }).also {
                compositeDisposable.add(it)
            }
    }

    private fun updateUi(message: String) {
        tvTime.text = message
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
