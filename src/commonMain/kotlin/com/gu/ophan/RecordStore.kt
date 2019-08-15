package com.gu.ophan

interface RecordStore {
    fun putRecord(key: String, record: ByteArray)
    fun getRecords(): List<ByteArray>
    fun removeRecord(key: String)
}

class InMemoryRecordStore : RecordStore {
    private val records = mutableMapOf<String, ByteArray>()

    override fun putRecord(key: String, record: ByteArray) {
        records[key] = record
    }

    override fun getRecords(): List<ByteArray> {
        return records.values.toList()
    }

    override fun removeRecord(key: String) {
        records.remove(key)
    }
}

expect class FileRecordStore(path: String) : RecordStore
