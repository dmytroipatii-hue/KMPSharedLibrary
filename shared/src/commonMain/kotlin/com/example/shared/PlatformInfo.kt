package com.example.shared
import kotlin.js.JsExport

@OptIn(kotlin.js.ExperimentalJsExport::class)
@JsExport
public class PlatformInfo {

    private val platform: Platform = platform()

    public fun getGreetingText(): String {
        return "Hello, ${platform.name}"
    }

    public fun getPlatformName(): String {
        println("Version 1.0.1")
        println("Platform Name: ${platform.name}")
        return platform.name
    }

    public fun getPlatformVersion(): Double {
        println("Platform Version: ${platform.version}")
        return platform.version
    }

}