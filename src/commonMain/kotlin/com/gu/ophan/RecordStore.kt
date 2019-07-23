package com.gu.ophan

interface RecordStore {
    fun putRecord(key: String, record: ByteArray)
    fun getRecords(): List<ByteArray>
    fun removeRecord(key: String)
}

expect class FileRecordStore(path: String) : RecordStore