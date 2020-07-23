package com.github.binpower93.hilttest.di

import com.github.binpower93.hilttest.model.network.PostApi
import com.github.binpower93.hilttest.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object NetworkModule {

    /**
     * Provides the Retrofit object
     * @return the Retrofit object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    /**
     * Provides the PostApi implementation
     * @param retrofit the Retrofit instance to instantiate the PostApi
     * @return the PostApi instance created
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun providePostApi(retrofit: Retrofit) = retrofit.create(PostApi::class.java)
}