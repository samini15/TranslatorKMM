package com.example.translatorkmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform