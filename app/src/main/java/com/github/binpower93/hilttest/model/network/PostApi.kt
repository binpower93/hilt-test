package com.github.binpower93.hilttest.model.network

import com.github.binpower93.hilttest.model.Post
import io.reactivex.Observable
import retrofit2.http.GET

interface PostApi {

    @GET("/posts.json")
    fun getPosts(): Observable<List<Post?>>
}