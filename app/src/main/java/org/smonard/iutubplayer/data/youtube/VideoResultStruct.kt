package org.smonard.iutubplayer.data.youtube

// Missing the struct keyword
class VideoResultStruct {

    lateinit var items: List<Item>

    inner class Item {

        lateinit var id: Id

        lateinit var snippet: Snippet

        inner class Id {

            lateinit var videoId: String
        }

        inner class Snippet {

            lateinit var publishedAt: String

            lateinit var channelTitle: String

            lateinit var title: String

            lateinit var thumbnails: Thumbnails

            inner class Thumbnails {

                lateinit var medium: Default

                inner class Default {

                    lateinit var url: String
                }
            }
        }
    }

}
