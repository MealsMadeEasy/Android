package com.mealsmadeeasy.data.service

import com.mealsmadeeasy.BuildConfig
import com.mealsmadeeasy.model.toDate
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://meals-made-easy.herokuapp.com"

fun createMealsMadeEasyApi(): MealsMadeEasyService = Retrofit.Builder()
        .apply {
            baseUrl(BASE_URL)
            addConverterFactory(MoshiConverterFactory.create(createMoshi()))
            addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            client(createClient())
        }
        .build()
        .create(MealsMadeEasyService::class.java)

private fun createClient(): OkHttpClient {
    return OkHttpClient.Builder().apply {
        addInterceptor {
            it.proceed(it.request().newBuilder()
                    .apply {
                        addHeader("ApplicationID", BuildConfig.API_APPLICATION_ID)
                        addHeader("ApiKey", BuildConfig.API_KEY)
                    }.build()
            )
        }
    }.build()
}

private fun createMoshi(): Moshi {
    return Moshi.Builder()
            .add(Unit::class.java, object : JsonAdapter<Unit>() {
                override fun fromJson(reader: JsonReader) = Unit

                override fun toJson(writer: JsonWriter, value: Unit?) {
                    if (value != null) {
                        writer.beginObject()
                        writer.endObject()
                    }
                }
            })
            .add(DateTime::class.java, object : JsonAdapter<DateTime>() {
                override fun fromJson(reader: JsonReader): DateTime {
                    return reader.nextLong().toDate()
                }

                override fun toJson(writer: JsonWriter, value: DateTime?) {
                    value?.let { writer.value(it.withZone(DateTimeZone.UTC).millis) }
                }

            })
            .build()
}