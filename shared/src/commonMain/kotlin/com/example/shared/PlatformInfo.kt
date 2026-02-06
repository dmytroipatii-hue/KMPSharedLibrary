package com.example.shared
import kotlin.js.JsExport

@JsExport
class PlatformInfo {

    private val platform: Platform = platform()

    fun getGreetingText(): String {
        return "Hello, ${platform.name}"
    }

    fun getPlatformName(): String {
        return platform.name
    }

    fun getPlatformVersion(): Double {
        return platform.version
    }

}