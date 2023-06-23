package com.adbsalam.user_plugin_runtime

@Suppress("unused")
object MetaLogger {

    /**
     * Called by [io.arrowkt.example.MetaPlugin] during IR transformation
     */
    @Suppress("unused")
    fun modifyHeader() {
        println("Compiler Plugin - Header - This is start of modified code")
    }

    @Suppress("unused")
    fun modifyEnd() {
        println("Compiler Plugin - End - This is end of modified code")
    }

}