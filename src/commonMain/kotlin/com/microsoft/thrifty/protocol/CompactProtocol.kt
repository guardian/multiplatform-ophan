/*
 * Thrifty
 *
 * Copyright (c) Microsoft Corporation
 *
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS CODE IS PROVIDED ON AN  *AS IS* BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING
 * WITHOUT LIMITATION ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE,
 * FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABLITY OR NON-INFRINGEMENT.
 *
 * See the Apache Version 2.0 License for specific language governing permissions and limitations under the License.
 */

/*
 * This file is derived from the file TCompactProtocol.java, in the Apache
 * Thrift implementation.  The original license header is reproduced
 * below:
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.microsoft.thrifty.protocol

import com.microsoft.thrifty.TType
import com.microsoft.thrifty.transport.Transport
import kotlinx.io.charsets.Charset
import kotlinx.io.charsets.Charsets
import kotlinx.io.core.EOFException
import kotlinx.io.core.String
import kotlinx.io.core.toByteArray
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
val SharedImmutableUtf8: Charset = Charsets.UTF_8

/**
 * An implementation of the Thrift compact binary protocol.
 *
 *
 * Instances of this class are *not* threadsafe.
 */
class CompactProtocol(transport: Transport) : Protocol(transport) {

    // Boolean fields get special treatment - their value is encoded
    // directly in the field header.  As such, when a boolean field
    // header is written, we cache it here until we get the value from
    // the subsequent `writeBool` call.
    private var booleanFieldId = -1

    // Similarly, we cache the value read from a field header until
    // the `readBool` call.
    private var booleanFieldType: Byte = -1

    private val buffer = ByteArray(16)

    // Keep track of the most-recently-written fields,
    // used for delta-encoding.
    private val writingFields = ShortStack()
    private var lastWritingField: Short = 0

    private val readingFields = ShortStack()
    private var lastReadingField: Short = 0

    override fun writeMessageBegin(name: String, typeId: Byte, seqId: Int) {
        writeByte(PROTOCOL_ID)
        writeByte((VERSION and VERSION_MASK or ((typeId * TYPE_SHIFT_MULTIPLIER).toByte() and TYPE_MASK)))
        writeVarint32(seqId)
        writeString(name)
    }

    override fun writeMessageEnd() {
        // no wire representation
    }

    override fun writeStructBegin(structName: String) {
        writingFields.push(lastWritingField)
        lastWritingField = 0
    }

    override fun writeStructEnd() {
        lastWritingField = writingFields.pop()
    }

    override fun writeFieldBegin(fieldName: String, fieldId: Int, typeId: Byte) {
        if (typeId == TType.BOOL) {
            if (booleanFieldId != -1) {
                throw RuntimeException("Nested invocation of writeFieldBegin")
            }
            booleanFieldId = fieldId
        } else {
            writeFieldBegin(fieldId, CompactTypes.ttypeToCompact(typeId))
        }
    }

    private fun writeFieldBegin(fieldId: Int, compactTypeId: Byte) {
        // Can we delta-encode the field ID?
        if (fieldId > lastWritingField && fieldId - lastWritingField <= 15) {
            val fieldIdDelta: Byte = (fieldId - lastWritingField).toByte()
            writeByte((fieldIdDelta shl 4) or compactTypeId)
        } else {
            writeByte(compactTypeId)
            writeI16(fieldId.toShort())
        }

        lastWritingField = fieldId.toShort()
    }

    override fun writeFieldEnd() {
        // no wire representation
    }

    override fun writeFieldStop() {
        writeByte(TType.STOP)
    }

    override fun writeMapBegin(keyTypeId: Byte, valueTypeId: Byte, mapSize: Int) {
        if (mapSize == 0) {
            writeByte(0.toByte())
        } else {
            val compactKeyType = CompactTypes.ttypeToCompact(keyTypeId)
            val compactValueType = CompactTypes.ttypeToCompact(valueTypeId)

            writeVarint32(mapSize)
            writeByte(compactKeyType shl 4 or compactValueType)
        }
    }

    override fun writeMapEnd() {
        // no wire representation
    }

    override fun writeListBegin(elementTypeId: Byte, listSize: Int) {
        writeVectorBegin(elementTypeId, listSize)
    }

    override fun writeListEnd() {
        // no wire representation
    }

    override fun writeSetBegin(elementTypeId: Byte, setSize: Int) {
        writeVectorBegin(elementTypeId, setSize)
    }

    override fun writeSetEnd() {
        // no wire representation
    }

    override fun writeBool(b: Boolean) {
        val compactValue = if (b) CompactTypes.BOOLEAN_TRUE else CompactTypes.BOOLEAN_FALSE
        if (booleanFieldId != -1) {
            // We are writing a boolean field, and need to write the
            // deferred field header.  In this case we encode the value
            // directly in the header's type field.
            writeFieldBegin(booleanFieldId, compactValue)
            booleanFieldId = -1
        } else {
            // We are not writing a field - just write the value directly.
            writeByte(compactValue)
        }
    }

    override fun writeByte(b: Byte) {
        buffer[0] = b
        transport.write(buffer, 0, 1)
    }

    override fun writeI16(i16: Short) {
        writeVarint32(intToZigZag(i16.toInt()))
    }

    override fun writeI32(i32: Int) {
        writeVarint32(intToZigZag(i32))
    }

    override fun writeI64(i64: Long) {
        writeVarint64(longToZigZag(i64))
    }

    override fun writeDouble(dub: Double) {
        //val bits = java.lang.Double.doubleToLongBits(dub)
        val bits: Long = dub.toBits()

        // Doubles get written out in little-endian order
        buffer[0] = (bits and 0xFFL).toByte()
        buffer[1] = (bits.ushr(8) and 0xFFL).toByte()
        buffer[2] = (bits.ushr(16) and 0xFFL).toByte()
        buffer[3] = (bits.ushr(24) and 0xFFL).toByte()
        buffer[4] = (bits.ushr(32) and 0xFFL).toByte()
        buffer[5] = (bits.ushr(40) and 0xFFL).toByte()
        buffer[6] = (bits.ushr(48) and 0xFFL).toByte()
        buffer[7] = (bits.ushr(56) and 0xFFL).toByte()

        transport.write(buffer, 0, 8)
    }

    override fun writeString(str: String) {
        val bytes = str.toByteArray(SharedImmutableUtf8)
        writeVarint32(bytes.size)
        transport.write(bytes)
    }

    override fun writeBinary(buf: ByteArray) {
        writeVarint32(buf.size)
        transport.write(buf)
    }

    private fun writeVectorBegin(typeId: Byte, size: Int) {
        val compactId = CompactTypes.ttypeToCompact(typeId)
        if (size <= 14) {
            writeByte((size shl 4 or compactId.toInt()).toByte())
        } else {
            writeByte((0xF0 or compactId.toInt()).toByte())
            writeVarint32(size)
        }
    }

    private fun writeVarint32(n: Int) {
        var nCopy = n
        for (i in buffer.indices) {
            if (nCopy and 0x7F.inv() == 0x00) {
                buffer[i] = nCopy.toByte()
                transport.write(buffer, 0, i + 1)
                return
            } else {
                buffer[i] = (nCopy and 0x7F or 0x80).toByte()
                nCopy = nCopy ushr 7
            }
        }

        // Unpossible
        throw IllegalArgumentException("Cannot represent $nCopy as a varint in 16 bytes or less")
    }

    private fun writeVarint64(n: Long) {
        var nCopy = n
        for (i in buffer.indices) {
            if (nCopy and 0x7FL.inv() == 0x00L) {
                buffer[i] = nCopy.toByte()
                transport.write(buffer, 0, i + 1)
                return
            } else {
                buffer[i] = (nCopy and 0x7F or 0x80).toByte()
                nCopy = nCopy ushr 7
            }
        }

        // Unpossible
        throw IllegalArgumentException("Cannot represent $nCopy as a varint in 16 bytes or less")
    }

    override fun readMessageBegin(): MessageMetadata {
        val protocolId = readByte()
        if (protocolId != PROTOCOL_ID) {
            throw RuntimeException(
                    "Expected protocol ID " + PROTOCOL_ID.toInt()
                            + " but got " + protocolId.toInt())
        }

        val versionAndType = readByte()
        val version = (VERSION_MASK and versionAndType)
        if (version != VERSION) {
            throw RuntimeException(
                    "Version mismatch; expected version " + VERSION
                            + " but got " + version)
        }

        val typeId = (versionAndType shr TYPE_SHIFT_AMOUNT and TYPE_BITS)
        val seqId = readVarint32()
        val name = readString()

        return MessageMetadata(name, typeId, seqId)
    }

    override fun readMessageEnd() {

    }

    override fun readStructBegin(): StructMetadata {
        readingFields.push(lastReadingField)
        lastReadingField = 0
        return NO_STRUCT
    }

    override fun readStructEnd() {
        lastReadingField = readingFields.pop()
    }

    override fun readFieldBegin(): FieldMetadata {
        val compactId = readByte()
        val typeId = CompactTypes.compactToTtype(compactId and 0x0F)
        if (compactId == TType.STOP) {
            return END_FIELDS
        }

        val fieldId: Short
        val modifier: Short = ((compactId.toInt() and 0xF0) ushr 4).toShort()
        if (modifier.toInt() == 0) {
            // This is not a field-ID delta - read the entire ID.
            fieldId = readI16()
        } else {
            fieldId = (lastReadingField + modifier).toShort()
        }

        if (typeId == TType.BOOL) {
            // the bool value is encoded in the lower nibble of the ID
            booleanFieldType = (compactId and 0x0F)
        }

        lastReadingField = fieldId

        return FieldMetadata("", typeId, fieldId)
    }

    override fun readFieldEnd() {

    }

    override fun readMapBegin(): MapMetadata {
        val size = readVarint32()
        val keyAndValueTypes = if (size == 0) 0 else readByte()

        val keyType = CompactTypes.compactToTtype((keyAndValueTypes shr 4 and 0x0F))
        val valueType = CompactTypes.compactToTtype((keyAndValueTypes and 0x0F))

        return MapMetadata(keyType, valueType, size)
    }

    override fun readMapEnd() {
        // Nothing on the wire
    }

    override fun readListBegin(): ListMetadata {
        val sizeAndType = readByte().toInt()
        var size = sizeAndType shr 4 and 0x0F
        if (size == 0x0F) {
            size = readVarint32()
        }
        val compactType = (sizeAndType and 0x0F).toByte()
        val ttype = CompactTypes.compactToTtype(compactType)
        return ListMetadata(ttype, size)
    }

    override fun readListEnd() {
        // Nothing on the wire
    }

    override fun readSetBegin(): SetMetadata {
        val sizeAndType = readByte().toInt()
        var size = sizeAndType shr 4 and 0x0F
        if (size == 0x0F) {
            size = readVarint32()
        }
        val compactType = (sizeAndType and 0x0F).toByte()
        val ttype = CompactTypes.compactToTtype(compactType)
        return SetMetadata(ttype, size)
    }

    override fun readSetEnd() {
        // Nothing on the wire
    }

    override fun readBool(): Boolean {
        val compactId: Byte
        if (booleanFieldType.toInt() != -1) {
            compactId = booleanFieldType
            booleanFieldType = -1
        } else {
            compactId = readByte()
        }

        return compactId == CompactTypes.BOOLEAN_TRUE
    }

    override fun readByte(): Byte {
        readFully(buffer, 1)
        return buffer[0]
    }

    override fun readI16(): Short {
        return zigZagToInt(readVarint32()).toShort()
    }

    override fun readI32(): Int {
        return zigZagToInt(readVarint32())
    }

    override fun readI64(): Long {
        return zigZagToLong(readVarint64())
    }

    override fun readDouble(): Double {
        readFully(buffer, 8)
        val bits: Long = (buffer[0].toLong()
                or (buffer[1].toLong() shl 8)
                or (buffer[2].toLong() shl 16)
                or (buffer[3].toLong() shl 24)
                or (buffer[4].toLong() shl 32)
                or (buffer[5].toLong() shl 40)
                or (buffer[6].toLong() shl 48)
                or (buffer[7].toLong() shl 56))
        return Double.fromBits(bits)
    }

    override fun readString(): String {
        val length = readVarint32()
        if (length == 0) {
            return ""
        }

        val bytes = ByteArray(length)
        readFully(bytes, length)
        return String(bytes, charset = SharedImmutableUtf8)
    }

    override fun readBinary(): ByteArray {
        val length = readVarint32()
        if (length == 0) {
            return ByteArray(0)
        }

        val bytes = ByteArray(length)
        readFully(bytes, length)
        return bytes
    }

    private fun readVarint32(): Int {
        var result = 0
        var shift = 0
        while (true) {
            val b = readByte().toInt()
            result = result or (b and 0x7F shl shift)
            if (b and 0x80 != 0x80) {
                return result
            }
            shift += 7
        }
    }

    private fun readVarint64(): Long {
        var result: Long = 0
        var shift = 0
        while (true) {
            val b = readByte().toInt()
            result = result or ((b and 0x7F).toLong() shl shift)
            if (b and 0x80 != 0x80) {
                return result
            }
            shift += 7
        }
    }

    private fun readFully(buffer: ByteArray, count: Int) {
        var toRead = count
        var offset = 0
        while (toRead > 0) {
            val read = transport.read(buffer, offset, toRead)
            if (read == -1) {
                throw EOFException("it is the end")
            }
            toRead -= read
            offset += read
        }
    }

    private class CompactTypes private constructor() {

        init {
            throw AssertionError("no instances")
        }

        companion object {
            internal val BOOLEAN_TRUE: Byte = 0x01
            internal val BOOLEAN_FALSE: Byte = 0x02
            internal val BYTE: Byte = 0x03
            internal val I16: Byte = 0x04
            internal val I32: Byte = 0x05
            internal val I64: Byte = 0x06
            internal val DOUBLE: Byte = 0x07
            internal val BINARY: Byte = 0x08
            internal val LIST: Byte = 0x09
            internal val SET: Byte = 0x0A
            internal val MAP: Byte = 0x0B
            internal val STRUCT: Byte = 0x0C

            internal fun ttypeToCompact(typeId: Byte): Byte {
                when (typeId) {
                    TType.STOP -> return TType.STOP
                    TType.VOID -> throw IllegalArgumentException("Unexpected VOID type")
                    TType.BOOL -> return BOOLEAN_TRUE
                    TType.BYTE -> return BYTE
                    TType.DOUBLE -> return DOUBLE
                    TType.I16 -> return I16
                    TType.I32 -> return I32
                    TType.I64 -> return I64
                    TType.STRING -> return BINARY
                    TType.STRUCT -> return STRUCT
                    TType.MAP -> return MAP
                    TType.SET -> return SET
                    TType.LIST -> return LIST
                    else -> throw IllegalArgumentException(
                            "Unknown TType ID: $typeId")
                }
            }

            internal fun compactToTtype(compactId: Byte): Byte {
                when (compactId) {
                    TType.STOP -> return TType.STOP
                    BOOLEAN_TRUE -> return TType.BOOL
                    BOOLEAN_FALSE -> return TType.BOOL
                    BYTE -> return TType.BYTE
                    I16 -> return TType.I16
                    I32 -> return TType.I32
                    I64 -> return TType.I64
                    DOUBLE -> return TType.DOUBLE
                    BINARY -> return TType.STRING
                    LIST -> return TType.LIST
                    SET -> return TType.SET
                    MAP -> return TType.MAP
                    STRUCT -> return TType.STRUCT
                    else -> throw IllegalArgumentException(
                            "Unknown compact type ID: $compactId")
                }
            }
        }
    }

    private class ShortStack internal constructor() {
        private var stack: ShortArray = ShortArray(16)
        private var top: Int = -1

        internal fun push(value: Short) {
            if (top + 1 == stack.size) {
                stack = stack.copyOf(stack.size shl 2)
            }

            stack[++top] = value
        }

        internal fun pop(): Short {
            return stack[top--]
        }
    }

    companion object {

        // Constants, as defined in TCompactProtocol.java

        private val PROTOCOL_ID = 0x82.toByte()
        private val VERSION: Byte = 1
        private val VERSION_MASK: Byte = 0x1F
        private val TYPE_MASK = 0xE0.toByte()
        private val TYPE_BITS: Byte = 0x07
        private val TYPE_SHIFT_AMOUNT = 5
        private val TYPE_SHIFT_MULTIPLIER: Byte = 32

        private val NO_STRUCT = StructMetadata("")
        private val END_FIELDS = FieldMetadata("", TType.STOP, 0.toShort())

        /**
         * Convert a twos-complement int to zigzag encoding,
         * allowing negative values to be written as varints.
         */
        private fun intToZigZag(n: Int): Int {
            return n shl 1 xor (n shr 31)
        }

        /**
         * Convert a twos-complement long to zigzag encoding,
         * allowing negative values to be written as varints.
         */
        private fun longToZigZag(n: Long): Long {
            return n shl 1 xor (n shr 63)
        }

        private fun zigZagToInt(n: Int): Int {
            return n.ushr(1) xor -(n and 1)
        }

        private fun zigZagToLong(n: Long): Long {
            return n.ushr(1) xor -(n and 1)
        }
    }
}

private infix fun Byte.shl(i: Int): Byte {
    return (this.toInt() shl i).toByte()
}

private infix fun Byte.shr(i: Int): Byte {
    return (this.toInt() shr i).toByte()
}
