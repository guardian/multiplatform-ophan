package com.gu.ophan

import kotlinx.cinterop.*
import platform.Foundation.*

@ExperimentalUnsignedTypes
actual class FileRecordStore actual constructor(path: String) : RecordStore {

    private val fileManager = NSFileManager.defaultManager
    private val recordDirectory: NSURL

    init {
        val documentsDirectoryUrl = throwError { errorPointer ->
            fileManager.URLForDirectory(
                NSDocumentDirectory,
                NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = errorPointer)
        } ?: throw RuntimeException("could not get documents directory url")
        recordDirectory = initRecordDirectory(documentsDirectoryUrl, path)
    }

    private fun initRecordDirectory(documentsDirectoryUrl: NSURL, path: String): NSURL {
        val url = documentsDirectoryUrl.append(path, isDirectory = true)
        throwError { errorPointer ->
            fileManager.createDirectoryAtURL(url, false, null, errorPointer)
        }
        url.path?.let {
            println("$it exists? ${fileManager.fileExistsAtPath(it)}")
        }
        return url
    }

    override fun putRecord(key: String, record: ByteArray) {
        val url = urlForKey(key)
        val data: NSData = record.toNSData()
        val success = data.writeToURL(url, atomically = true)
        if (!success) {
            println("Error writing to $url")
        }
    }

    override fun getRecords(): List<ByteArray> {
        return fileManager
            .contentsOfDirectoryAtURL(recordDirectory, null, 0.toULong(), null)
            ?.mapNotNull { it as? NSURL }
            ?.mapNotNull { it.URLByAppendingPathComponent("wrong") }
            ?.mapNotNull { url ->
                try {
                    throwError { errorPointer ->
                        NSData.dataWithContentsOfURL(url, 0.toULong(), errorPointer)
                    }
                } catch (e: NSErrorException) {
                    println("Error reading from $url: ${e.nsError}")
                    null
                }
            }
            ?.mapNotNull { it.toByteArray() }
            ?: emptyList()
    }

    override fun removeRecord(key: String) {
        fileManager.removeItemAtURL(urlForKey(key) ?: return, null)
    }

    private fun urlForKey(key: String): NSURL {
        return recordDirectory.append(key)
    }

    private fun ByteArray.toNSData(): NSData {
        return usePinned {
            NSData.dataWithBytes(it.addressOf(0), it.get().size.toULong())
        }
    }

    private fun NSData.toByteArray(): ByteArray? {
        return this.bytes?.readBytes(this.length.toInt())
    }

    private fun NSURL.append(component: String, isDirectory: Boolean = false): NSURL {
        return URLByAppendingPathComponent(component, isDirectory)
            ?: throw RuntimeException("could not create record directory url")
    }
}

@Throws(NSErrorException::class)
fun <T> throwError(block: (errorPointer: CPointer<ObjCObjectVar<NSError?>>) -> T): T {
    memScoped {
        val errorPointer: CPointer<ObjCObjectVar<NSError?>> = alloc<ObjCObjectVar<NSError?>>().ptr
        val result: T = block(errorPointer)
        val error: NSError? = errorPointer.pointed.value
        if (error != null) {
            throw NSErrorException(error)
        } else {
            return result
        }
    }
}

class NSErrorException(val nsError: NSError): Exception(nsError.toString())
