package com.example.loader.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loader.R
import com.example.loader.adapter.ContactAdapter
import com.example.loader.loader.ContactLoader
import com.example.loader.model.ContactModel
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : AppCompatActivity(),
    LoaderManager.LoaderCallbacks<ArrayList<ContactModel>> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        init()
    }

    private fun init() {
        val contactLoaderManager =LoaderManager.getInstance(this)
        contactLoaderManager.initLoader(1, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<ContactModel>> {
        pbWaiting.visibility=View.VISIBLE
        return ContactLoader(this)
    }

    override fun onLoadFinished(
        loader: Loader<ArrayList<ContactModel>>,
        data: ArrayList<ContactModel>
    ) {
        val contactAdapter = ContactAdapter(this, data)
        rvContactsList.layoutManager = LinearLayoutManager(this)
        rvContactsList.adapter = contactAdapter
        pbWaiting.visibility=View.GONE
    }

    override fun onLoaderReset(loader: Loader<ArrayList<ContactModel>>) {
        TODO("Not yet implemented")
    }
}