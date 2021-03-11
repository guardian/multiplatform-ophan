package com.gu.ophan


actual class FileRecordStore actual constructor(path: String): RecordStore {
    override fun putRecord(key: String, record: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun getRecords(): List<ByteArray> {
        TODO("Not yet implemented")
    }

    override fun removeRecord(key: String) {
        TODO("Not yet implemented")
    }
}