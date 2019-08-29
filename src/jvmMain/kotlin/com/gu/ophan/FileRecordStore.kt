package com.gu.ophan

import com.gu.ophan.RecordStore
import java.io.File

actual class FileRecordStore actual constructor(path: String): RecordStore {

    // Get the record store directory and ensure it exists:
    private val directory = File(path).apply { mkdirs() }

    override fun putRecord(key: String, record: ByteArray) {
        recordFile(key).writeBytes(record)
    }

    override fun getRecords(): List<ByteArray> {
        val recordFiles = directory.listFiles().orEmpty()
        return recordFiles.map {
            it.readBytes()
        }
    }

    override fun removeRecord(key: String) {
        recordFile(key).delete()
    }

    private fun recordFile(key: String) = File(directory, key)
}
