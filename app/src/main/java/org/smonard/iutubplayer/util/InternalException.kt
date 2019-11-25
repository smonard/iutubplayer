package org.smonard.iutubplayer.util

class InternalException private constructor(message: String) : RuntimeException(message) {

    constructor() : this("Internal Error :(")
}
