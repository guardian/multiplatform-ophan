package com.gu.ophan

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.usePinned
import platform.Foundation.*

actual class FileRecordStore actual constructor(path: String) : RecordStore {

    private val fileManager = NSFileManager.defaultManager
    private val recordDirectory: NSURL

    init {
        println("Creating iOS FileRecordStore")
        val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
        val documentsDirectoryUrl = urls.firstOrNull() as? NSURL ?: TODO("handle this case")
        recordDirectory = documentsDirectoryUrl.URLByAppendingPathComponent(path, isDirectory = true) ?: TODO("handle this case")
        fileManager.createDirectoryAtURL(recordDirectory, false, null, null)
        recordDirectory.path?.let {
            println("${recordDirectory.path} exists? ${fileManager.fileExistsAtPath(it)}")
        }
        println("Created iOS FileRecordStore...")
    }

    override fun putRecord(key: String, record: ByteArray) {
        val data: NSData = record.toNSData()
        data.writeToURL(urlForKey(key), atomically = true)
    }

    override fun getRecords(): List<ByteArray> {
        return fileManager
            .contentsOfDirectoryAtURL(recordDirectory, null, 0.toULong(), null)
            ?.mapNotNull { it as? NSURL }
            ?.mapNotNull { url ->
                NSData.dataWithContentsOfURL(url, 0.toULong(), null)
            }
            ?.mapNotNull { it.toByteArray() }
            ?: emptyList()
    }

    override fun removeRecord(key: String) {
        fileManager.removeItemAtURL(urlForKey(key), null)
    }

    private fun urlForKey(key: String): NSURL {
        return recordDirectory.URLByAppendingPathComponent(key) ?: TODO("handle this case")
    }

    private fun ByteArray.toNSData(): NSData {
        return usePinned {
            NSData.dataWithBytes(it.addressOf(0), it.get().size.toULong())
        }
    }

    private fun NSData.toByteArray(): ByteArray? {
        return this.bytes?.readBytes(this.length.toInt())
    }
}