package org.smonard.iutubplayer.helpers

import org.jeasy.random.util.ReflectionUtils

object ReflectionTestUtils {
    fun setFieldValue(target: Any, fieldName: String?, newValue: Any?) {
        try {
            ReflectionUtils.setProperty(
                target,
                target.javaClass.getDeclaredField(fieldName!!),
                newValue
            )
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        }
    }

    fun setStaticFieldValue(target: Class<*>, fieldName: String?, newValue: Any?) {
        try {
            ReflectionUtils.setProperty(
                target,
                target.getDeclaredField(fieldName!!),
                newValue
            )
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        }
    }

    fun <U> getFieldValue(target: Any, fieldName: String?, fieldDataType: Class<U>): U {
        return try {
            val fieldValue = ReflectionUtils.getFieldValue(
                target,
                target.javaClass.getDeclaredField(fieldName!!)
            )
            fieldDataType.cast(fieldValue)!!
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        }
    }

    fun <U> getStaticFieldValue(target: Class<*>, fieldName: String?, fieldDataType: Class<U>): U {
        return try {
            val fieldValue = ReflectionUtils.getFieldValue(
                target,
                target.getDeclaredField(fieldName!!)
            )
            fieldDataType.cast(fieldValue)!!
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        }
    }
}