package org.smonard.iutubplayer.data.youtube

class ErrorResultStruct {
    @kotlin.jvm.JvmField
    var error: ApiError? = null

    inner class ApiError {

        @kotlin.jvm.JvmField
        var code: Int = 0

        @kotlin.jvm.JvmField
        var message: String? = null
    }
}
