package com.example.shared
import kotlin.js.JsExport

@OptIn(kotlin.js.ExperimentalJsExport::class)
@JsExport
class PlatformInfo {

    private val platform: Platform = platform()

    fun getGreetingText(): String {
        return "Hello, ${platform.name}"
    }

    fun getPlatformName(): String {
        println("Version 1.0.0")
        println("Platform Name: ${platform.name}")
        return platform.name
    }

    fun getPlatformVersion(): Double {
        println("Platform Version: ${platform.version}")
        return platform.version
    }

}