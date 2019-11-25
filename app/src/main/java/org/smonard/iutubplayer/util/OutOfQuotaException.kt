package org.smonard.iutubplayer.util

class OutOfQuotaException private constructor(message: String) : YoutubeApiException(message) {

    constructor() : this("You ran out of quota (Youtube Server). Try again later.")
}
