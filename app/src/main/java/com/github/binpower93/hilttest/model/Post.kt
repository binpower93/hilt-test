package com.github.binpower93.hilttest.model

/**
 * A class to contain the details of the data from the api
 * @constructor Sets all properties of the post
 * @property id the id of the post
 * @property title the given name of the post
 * @property contents the contents of the post
 */
data class Post(
    val id: String,
    val title: String,
    val contents: String
)