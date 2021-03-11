package com.vjezba.weatherapi

import android.app.Application
import com.vjezba.weatherapi.connectivity.network.ConnectivityChangedEvent
import com.vjezba.weatherapi.connectivity.network.ConnectivityMonitor
import dagger.hilt.android.HiltAndroidApp
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import org.greenrobot.eventbus.EventBus

@HiltAndroidApp
class App : Application() {

    lateinit var flutterEngine : FlutterEngine

    var getCurrentLocationOnlyOnce: Boolean

    init {
        ref = this
        getCurrentLocationOnlyOnce = false
    }

    companion object {
        @JvmStatic
        lateinit var ref: App
    }

    //event bus initialization
    val eventBus: EventBus by lazy {
        EventBus.builder()
            .logNoSubscriberMessages(false)
            .sendNoSubscriberEvent(false)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        //instance = this


        // Instantiate a FlutterEngine.
        flutterEngine = FlutterEngine(this)

        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Cache the FlutterEngine to be used by FlutterActivity.
        FlutterEngineCache
            .getInstance()
            .put("my_engine_id", flutterEngine)

        ConnectivityMonitor.initialize(this) { available ->
            eventBus.post(
                ConnectivityChangedEvent(
                    available
                )
            )
        }

    }

}

