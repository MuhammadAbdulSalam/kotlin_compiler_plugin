package com.adbsalam.user_plugin

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.invoke
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.FqName

val Meta.addLoggingToAnnotatedFunctions: CliPlugin
    get() = "Add Logging To Annotated Functions" {
        meta(
            irFunction { declaration ->
                return@irFunction if (declaration.hasAnnotation(FqName("com.adbsalam.user_plugin_runtime.MetaLog"))) {
                    declaration.body = prependLoggingToBody(pluginContext, declaration)

                    declaration
                } else {
                    declaration
                }
            }
        )
    }

fun prependLoggingToBody(pluginContext: IrPluginContext, declaration: IrFunction): IrBody {
    return DeclarationIrBuilder(pluginContext, declaration.symbol).irBlockBody {

        val referenceClass = pluginContext.referenceClass(FqName("com.adbsalam.user_plugin_runtime.MetaLogger"))
            ?: throw NoClassDefFoundError("MetaLogger not found")

        val header = referenceClass.getSimpleFunction("modifyHeader")
            ?: throw NoClassDefFoundError("MetaLogger.log() not found")

        val end = referenceClass.getSimpleFunction("modifyEnd")
            ?: throw NoClassDefFoundError("MetaLogger.log() not found")

        +irCall(header).apply {
            dispatchReceiver = irGetObject(referenceClass)
        }

        for (statement in declaration.body!!.statements) +statement

        +irCall(end).apply {
            dispatchReceiver = irGetObject(referenceClass)
        }
    }
}
