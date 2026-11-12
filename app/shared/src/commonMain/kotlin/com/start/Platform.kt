package com.start

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform