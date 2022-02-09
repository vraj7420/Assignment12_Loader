package com.example.loader.view

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loader.R
import com.example.loader.adapter.MusicAdapter
import com.example.loader.model.MusicModel
import kotlinx.android.synthetic.main.activity_music.*

class MusicActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    private var musicList = ArrayList<MusicModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        init()
    }

    private fun init() {
        val contactLoaderManager =LoaderManager.getInstance(this)
        contactLoaderManager.initLoader(1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        pbWaiting.visibility=View.VISIBLE
        val allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        return CursorLoader(this, allSongsUri, null, selection, null, null)

    }

    @SuppressLint("Range")
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        musicList.clear()
        if (data != null) {
            if (data.moveToFirst()) {
                do {
                    val songName: String =
                        data.getString(data.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    data.getInt(data.getColumnIndex(MediaStore.Audio.Media._ID))
                    val artistName: String = data.getString(data.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    musicList.add(MusicModel(songName, artistName))

                } while (data.moveToNext())
            }
        }
        setMusicAdapter()
    }
    private fun setMusicAdapter(){
        val musicAdapter= MusicAdapter(this,musicList)
        rvMusic.layoutManager = LinearLayoutManager(this)
        rvMusic.adapter=musicAdapter
        pbWaiting.visibility=View.GONE
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        }
    }