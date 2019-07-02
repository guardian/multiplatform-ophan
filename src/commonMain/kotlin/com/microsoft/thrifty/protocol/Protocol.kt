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
package com.microsoft.thrifty.protocol

import com.microsoft.thrifty.transport.Transport

abstract class Protocol protected constructor(val transport: Transport) {

    abstract fun writeMessageBegin(name: String, typeId: Byte, seqId: Int)

    abstract fun writeMessageEnd()

    abstract fun writeStructBegin(structName: String)

    abstract fun writeStructEnd()

    abstract fun writeFieldBegin(fieldName: String, fieldId: Int, typeId: Byte)

    abstract fun writeFieldEnd()

    abstract fun writeFieldStop()

    abstract fun writeMapBegin(keyTypeId: Byte, valueTypeId: Byte, mapSize: Int)

    abstract fun writeMapEnd()

    abstract fun writeListBegin(elementTypeId: Byte, listSize: Int)

    abstract fun writeListEnd()

    abstract fun writeSetBegin(elementTypeId: Byte, setSize: Int)

    abstract fun writeSetEnd()

    abstract fun writeBool(b: Boolean)

    abstract fun writeByte(b: Byte)

    abstract fun writeI16(i16: Short)

    abstract fun writeI32(i32: Int)

    abstract fun writeI64(i64: Long)

    abstract fun writeDouble(dub: Double)

    abstract fun writeString(str: String)

    abstract fun writeBinary(buf: ByteArray)

    ////////

    abstract fun readMessageBegin(): MessageMetadata

    abstract fun readMessageEnd()

    abstract fun readStructBegin(): StructMetadata

    abstract fun readStructEnd()

    abstract fun readFieldBegin(): FieldMetadata

    abstract fun readFieldEnd()

    abstract fun readMapBegin(): MapMetadata

    abstract fun readMapEnd()

    abstract fun readListBegin(): ListMetadata

    abstract fun readListEnd()

    abstract fun readSetBegin(): SetMetadata

    abstract fun readSetEnd()

    abstract fun readBool(): Boolean

    abstract fun readByte(): Byte

    abstract fun readI16(): Short

    abstract fun readI32(): Int

    abstract fun readI64(): Long

    abstract fun readDouble(): Double

    abstract fun readString(): String

    abstract fun readBinary(): ByteArray

    //////////////

    open fun flush() {
        transport.flush()
    }

    open fun reset() {
        // to be implemented by children as needed
    }

    fun close() {
        this.transport.close()
    }
}
