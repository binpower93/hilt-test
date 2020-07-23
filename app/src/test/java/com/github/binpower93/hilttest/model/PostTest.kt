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
}