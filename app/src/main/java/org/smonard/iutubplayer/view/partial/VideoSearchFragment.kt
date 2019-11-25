package org.smonard.iutubplayer.view.partial

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import com.google.android.material.textfield.TextInputLayout
import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.process.main.VideoListProcessor
import org.smonard.iutubplayer.process.main.VideoSearcher
import org.smonard.iutubplayer.process.support.InstanceHolder
import org.smonard.iutubplayer.process.support.InstanceHolder.getVideosProcessor

class VideoSearchFragment : VideoListFragment() {
    private val videoSearcher: VideoSearcher? = getVideosProcessor(
        InstanceHolder.Processor.SEARCH,
        VideoSearcher::class.java
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_video_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listView.onItemClickListener = this
        listAdapter = videoSearcher!!.getVideoListAdapter(activity)
        registerForContextMenu(listView)
        val editText =
            (activity.findViewById<View>(R.id.search_box) as TextInputLayout).editText
        editText!!.imeOptions = EditorInfo.IME_ACTION_DONE
        editText.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadSearch(v.text.toString())
                hideKeyboard(v)
                return@OnEditorActionListener true
            }
            false
        })
        editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun loadSearch(query: String) {
        videoSearcher!!.search(query)
    }

    override val videoListProcessor: VideoListProcessor
        get() = videoSearcher!!

    override val contextMenuId: Int
        get() = R.menu.video_menu

}