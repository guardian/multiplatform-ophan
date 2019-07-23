package com.gu.ophan

import com.gu.ophan.RecordStore

actual class FileRecordStore actual constructor(path: String) : RecordStore {
    override fun putRecord(key: String, record: ByteArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRecords(): List<ByteArray> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeRecord(key: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}