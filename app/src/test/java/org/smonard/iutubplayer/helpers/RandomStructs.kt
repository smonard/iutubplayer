package org.smonard.iutubplayer.helpers

import org.jeasy.random.EasyRandom
import org.smonard.iutubplayer.process.support.Video
import java.util.*

class RandomStructs : EasyRandom() {

    fun getPlaylist(size: Int): List<Video> {
        val list: MutableList<Video> =
            ArrayList(size)
        for (i in 0 until size) {
            list.add(nextObject(Video::class.java))
        }
        return list
    }

    fun <E> getList(type: Class<E>?, size: Int): List<E> {
        val list: MutableList<E> = ArrayList(size)
        for (i in 0 until size) {
            list.add(nextObject(type))
        }
        return list
    }

    fun randomString(): String {
        return nextObject(String::class.java)
    }

    fun randomUrl(): String {
        return String.format("https://%s", randomString())
    }
}