package com.start.models

import kotlinx.serialization.Serializable

@Serializable
data class Todos(
    val id: Int,
    val name: String,
)