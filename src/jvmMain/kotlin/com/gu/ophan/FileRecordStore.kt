package com.gu.ophan

import com.gu.ophan.RecordStore
import java.io.File

actual class FileRecordStore actual constructor(path: String): RecordStore {

    // Get the record store directory and ensure it exists:
    private val directory = File(path).apply { mkdirs() }

    override fun putRecord(key: String, record: ByteArray) {
        val file = recordFile(key)
        // recordstore file is in 'cache'git and can be delete by OS, create it if not exists
        if(!directory.exists()) {
            file.mkdirs()
        }

        file.writeBytes(record)
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
