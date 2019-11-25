package org.smonard.iutubplayer.process.support

import android.graphics.Bitmap

class Video {

    lateinit var id: String

    lateinit var title: String

    lateinit var publishedAt: String

    lateinit var channelId: String

    lateinit var previewUrl: String

    @Transient
    var preview: Bitmap? = null

    constructor(videoId: String, channelId: String, publishedAt: String, title: String, previewUrl: String) {
        this.id = videoId
        this.channelId = channelId
        this.publishedAt = publishedAt
        this.title = title
        this.previewUrl = previewUrl
    }

    constructor(videoId: String) {
        this.id = videoId
    }

    constructor() {}
}
