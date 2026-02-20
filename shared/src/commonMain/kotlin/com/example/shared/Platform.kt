package com.example.shared

public interface Platform {
    public val name: String
    public val version: Double
}

public expect fun platform(): Platform