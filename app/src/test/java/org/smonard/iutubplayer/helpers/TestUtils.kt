package org.smonard.iutubplayer.helpers

import org.mockito.Mockito
import kotlin.reflect.KClass

object TestUtils {
    fun <T : Any> any(kClass: KClass<T>): T {
        return castNull()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> castNull(): T = null as T

    fun <T> eq(value: T): T {
        return Mockito.eq(value) ?: value
    }


}