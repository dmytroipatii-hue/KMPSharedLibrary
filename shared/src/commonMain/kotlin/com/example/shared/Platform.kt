package com.example.shared

interface Platform {
    val name: String
    val version: Double
}

expect fun platform(): Platform