package com.adbsalam.user_plugin_runtime

@Suppress("unused")
object MetaLogger {

    /**
     * Called by [io.arrowkt.example.MetaPlugin] during IR transformation
     */
    @Suppress("unused")
    fun log() {
        println("MetaLogger.log() invoked")
    }
}