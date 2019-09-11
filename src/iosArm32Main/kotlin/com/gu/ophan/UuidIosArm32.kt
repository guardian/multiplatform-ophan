package com.gu.ophan

import platform.Foundation.NSUUID

actual fun newUuidV4() = NSUUID().UUIDString
