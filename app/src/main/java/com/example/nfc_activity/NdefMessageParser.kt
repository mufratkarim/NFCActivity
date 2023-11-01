package com.example.nfc_activity

import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.nfc_activity.record.ParsedNdefRecord
import com.example.nfc_activity.record.SmartPoster
import com.example.nfc_activity.record.TextRecord
import com.example.nfc_activity.record.UriRecord

/**
 * Utility class for creating [ParsedNdefMessage]s.
 */
object NdefMessageParser {
    /** Parse an NdefMessage  */
    fun parse(message: NdefMessage): List<ParsedNdefRecord> {
        return getRecords(message.records)
    }

    fun getRecords(records: Array<NdefRecord>): List<ParsedNdefRecord> {
        val elements = mutableListOf<ParsedNdefRecord>()
        for (record in records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record))
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record))
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record))
            } else {
                elements.add(object : ParsedNdefRecord {
                    override fun getView(
                        activity: Activity,
                        inflater: LayoutInflater,
                        parent: ViewGroup,
                        offset: Int
                    ): View {
                        val text = inflater.inflate(R.layout.tag_text, parent, false) as TextView
                        text.text = String(record.payload)
                        return text
                    }
                })
            }
        }
        return elements
    }
}