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
package com.microsoft.thrifty.transport

import io.ktor.utils.io.core.*


class BufferTransport(val b: IoBuffer) : Transport() {

    override fun read(buffer: ByteArray, offset: Int, count: Int): Int {
        /*
         The original Java implementation of this method was:

             return b.read(buffer, offset, count);

         where `b` was an Okio `Buffer`, with the `Buffer.read` method having this documentation:

             Removes up to `count` bytes from this and copies them into `buffer` at `offset`.
             Returns the number of bytes read, or -1 if this source is exhausted.

         This seems like it is analogous to `IoBuffer.readAvailable`:
         */
        return b.readAvailable(buffer, offset, count)
    }

    override fun write(buffer: ByteArray, offset: Int, count: Int) {
        b.writeFully(buffer, offset, count)
    }

    override fun flush() {
        b.flush()
    }

    override fun close() {
        b.close()
    }
}
