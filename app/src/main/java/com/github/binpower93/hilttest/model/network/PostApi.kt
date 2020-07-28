package com.github.binpower93.hilttest.model.network

import com.github.binpower93.hilttest.model.Post
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PostApi {

    @GET("/posts.json")
    fun getPosts(): Observable<Map<String, Post>?>

    @POST("/posts.json")
    fun insertPost(@Body post: Post): Observable<Map<String, String>>
}