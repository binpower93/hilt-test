package com.github.binpower93.hilttest.model

import junit.framework.TestCase

class PostTest : TestCase() {

    fun testThatCreatingPostReturnsExpectedValues() {
        val post = Post(
            id = "test-id",
            title = "Test Title",
            contents = "Test Content"
        )

        assertEquals("test-id", post.id)
        assertEquals("Test Title", post.title)
        assertEquals("Test Content", post.contents)
    }

    fun testThatTwoPostsWithTheSamePropertiesAreEqual() {
        val post1 = Post(
            id = "test-id",
            title = "Test Title",
            contents = "Test Content"
        )

        val post2 = Post(
            id = "test-id",
            title = "Test Title",
            contents = "Test Content"
        )

        assertEquals(post1, post2)
    }

    fun testThatTwoPostsWithTheSamePropertiesHaveHashCodes() {
        val post1 = Post(
            id = "test-id",
            title = "Test Title",
            contents = "Test Content"
        )

        val post2 = Post(
            id = "test-id",
            title = "Test Title",
            contents = "Test Content"
        )

        assertEquals(post1.hashCode(), post2.hashCode())
    }
}