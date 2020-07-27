package com.example.simplevideoview

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

  /*  private val VIDEO_SAMPLE = "tacoma_narrows"*/
    private var mVideoView: VideoView? = null
    private var mCurrentPosition = 0
    private val PLAYBACK_TIME = "play_time"
    private val VIDEO_SAMPLE =
        "https://developers.google.com/training/images/tacoma_narrows.mp4"
    private var loadingProgressBar: ProgressBar? = null
    private var playButton: Button? = null
    private var pauseButton: Button? = null
    private var rewindButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialization()

        onClick()

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }
        val controller = MediaController(this)
        controller.setMediaPlayer(mVideoView)
        mVideoView!!.setMediaController(controller)

    }


    private fun initialization(){

        mVideoView = findViewById(R.id.videoView)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        playButton = findViewById(R.id.play_button)
        pauseButton = findViewById(R.id.pause_button)
        rewindButton = findViewById(R.id.rewind_button)
    }

    private fun onClick(){
        playButton!!.setOnClickListener{
            mVideoView!!.start()
        }
        pauseButton!!.setOnClickListener{
            mVideoView!!.pause()
        }
        rewindButton!!.setOnClickListener{
            mVideoView!!.seekTo(1)
            mVideoView!!.start()
        }


    }

    private fun String.getMedia(): Uri? {
        return if (URLUtil.isValidUrl(this)) {
            Uri.parse(this)
        } else {
            Uri.parse("android.resource://" + packageName +
                    "/raw/" + this
            );
        }
    }

    private fun initializePlayer() {

        mVideoView!!.setBackgroundColor(Color.WHITE)
        val videoUri = VIDEO_SAMPLE.getMedia()
        mVideoView!!.setVideoURI(videoUri)

        mVideoView!!.setOnPreparedListener { mp ->
            mp.setOnBufferingUpdateListener { mp, percent ->
                Log.d("lsknflsnf","Percent $percent")
                if (percent == 100) {
                  mVideoView!!.setBackgroundColor(Color.TRANSPARENT)
                    loadingProgressBar!!.visibility = VideoView.INVISIBLE

                }
            }
        }
        mVideoView!!.setOnCompletionListener {
            Toast.makeText(this, "Playback completed",
                Toast.LENGTH_SHORT).show();
            mVideoView!!.seekTo(1);
        }
    }

    private fun releasePlayer() {
        mVideoView!!.stopPlayback()
    }

    @Override
    override fun onStart() {
        super.onStart()
       initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        mVideoView!!.pause()

    }

    @Override
    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PLAYBACK_TIME, mVideoView!!.currentPosition)
    }
}