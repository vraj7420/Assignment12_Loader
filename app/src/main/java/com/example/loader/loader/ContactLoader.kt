package com.example.loader.loader

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.loader.content.AsyncTaskLoader
import com.example.loader.model.ContactModel

class ContactLoader(context: Context) : AsyncTaskLoader<ArrayList<ContactModel>>(context) {
    private val contactList = ArrayList<ContactModel>()

    @SuppressLint("Range")
    override fun loadInBackground(): ArrayList<ContactModel> {
        contactList.clear()
        val cr: ContentResolver = context.contentResolver!!
        val sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        val cur: Cursor? = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, sort)
        var phoneNo: String
        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String =
                    cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String =
                    cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val photoIndex =
                        cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                    val uri: String? = cur.getString(photoIndex)
                    val phoneCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )

                    while (phoneCur!!.moveToNext()) {
                        phoneNo =
                            phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        if (uri != null) {
                            contactList.add(ContactModel(name, phoneNo, uri))
                        } else {
                            contactList.add(ContactModel(name, phoneNo, Uri.parse("android.resource://com.example.loader/drawable/ic_person").toString()))
                        }
                    }
                    phoneCur.close()
                }
            }
        }
        cur?.close()
        return contactList
    }

    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }
}