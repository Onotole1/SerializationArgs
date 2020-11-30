package com.example.serializationargs

import kotlinx.serialization.*

@Serializable
data class FeedArguments(
    val postId: Long,
    val content: String,
    val list: List<Test> = listOf(
        Test()
    )
)

@Serializable
data class Test(
    val id: Long = 999L
)
