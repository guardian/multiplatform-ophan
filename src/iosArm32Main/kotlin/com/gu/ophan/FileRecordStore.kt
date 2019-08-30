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

    private fun initRecordDirectory(parentDirectory: NSURL, path: String): NSURL {
        val url = parentDirectory.append(path, isDirectory = true)
        url.path?.let {
            if (!fileManager.fileExistsAtPath(it)) {
                println("$it does not exist, creating it...")
                throwError { errorPointer ->
                    fileManager.createDirectoryAtURL(url, false, null, errorPointer)
                }
            }
            println("Confirm $it exists? ${fileManager.fileExistsAtPath(it)}")
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
            .contentsOfDirectoryAtURL(recordDirectory, null, 0.toUInt(), null)
            ?.mapNotNull { it as? NSURL }
            ?.mapNotNull { url ->
                try {
                    throwError { errorPointer ->
                        NSData.dataWithContentsOfURL(url, 0.toUInt(), errorPointer)
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
        fileManager.removeItemAtURL(urlForKey(key), null)
    }

    private fun urlForKey(key: String): NSURL {
        return recordDirectory.append(key)
    }

    /**
     * Return these bytes as an [NSData].
     */
    private fun ByteArray.toNSData(): NSData {
        return usePinned {
            NSData.dataWithBytes(it.addressOf(0), it.get().size.toUInt())
        }
    }

    /**
     * Return this data as a [ByteArray].
     */
    private fun NSData.toByteArray(): ByteArray? {
        return this.bytes?.readBytes(this.length.toInt())
    }

    /**
     * Append a path component and return the resulting [NSURL], or throw a [RuntimeException] if that would give null.
     */
    private fun NSURL.append(component: String, isDirectory: Boolean = false): NSURL {
        return URLByAppendingPathComponent(component, isDirectory)
            ?: throw RuntimeException("could not create record directory url")
    }
}

/**
 * Helper method allowing any [NSError] error that occurs within [block] to be thrown as an exception.
 *
 * Apple's Objective-C libraries have their own error-handling idiom. Various functions accept a parameter called
 * `error` which is a pointer and these functions will make that pointer point to an [NSError] if something goes wrong.
 * In Kotlin, using a pointer in this way is cumbersome so this helper handles the heavy lifting and let's you write
 * code like this:
 *
 *     throwError { errorPointer ->
 *         NSData.dataWithContentsOfURL(url, 0.toULong(), errorPointer)
 *     }
 *
 * which either evaluates to the expected [NSData] returned by `dataWithContentsOfURL` or throws an [NSErrorException]
 * if something went wrong. This exception can be caught and handled in the normal way.
 */
// We can't use the @Throws annotation here due to a compiler bug which will be fixed in 1.3.50:
// https://youtrack.jetbrains.com/issue/KT-28927
//@Throws(NSErrorException::class)
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
