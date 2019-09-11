package com.gu.ophan

import platform.Foundation.NSUUID

actual fun newUuidV4(): String {
    return NSUUID().UUIDString
}
