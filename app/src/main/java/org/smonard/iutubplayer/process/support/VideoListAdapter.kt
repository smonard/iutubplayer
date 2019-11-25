package org.smonard.iutubplayer.process.support

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.data.youtube.PreviewLoader
import java.util.function.Function

class VideoListAdapter(context: Context, objects: List<Video>) : ArrayAdapter<Video>(context, R.layout.row_video_list, objects) {

    private class ViewHolder {
        internal var titleView: TextView? = null
        internal var channelView: TextView? = null
        internal var preView: ImageView? = null
        internal var cache: ImageView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView1 = convertView
        val video = getItem(position)

        val viewHolder: ViewHolder

        if (convertView1 == null) {
            convertView1 = LayoutInflater.from(context).inflate(R.layout.row_video_list, parent, false)
            viewHolder = ViewHolder()
            viewHolder.titleView = convertView1!!.findViewById(R.id.title)
            viewHolder.channelView = convertView1.findViewById(R.id.channel)
            viewHolder.preView = convertView1.findViewById(R.id.preview)
            viewHolder.cache = ImageView(context)
            convertView1.tag = viewHolder
        } else {
            viewHolder = convertView1.tag as ViewHolder
        }

        viewHolder.titleView!!.text = video!!.title
        viewHolder.channelView!!.text = video.channelId
        viewHolder.preView!!.setImageBitmap(video.preview)

        if (video.preview == null) {
            try {
                PreviewLoader(video.previewUrl, callback(video, viewHolder)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ErrorImgLoadInView", e.message, e)
            }

        }
        return convertView1
    }

    private fun callback(video: Video, viewHolder: ViewHolder): Function<Bitmap, Void?> {
        return Function { image ->
            video.preview = image
            viewHolder.preView!!.setImageBitmap(image)
            null
        }
    }

}
