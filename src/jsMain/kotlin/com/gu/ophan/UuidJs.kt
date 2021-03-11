package com.gu.ophan

@JsModule("uuid")
@JsNonModule
@JsName("v4")
external fun jsUuidV4(): String

actual fun newUuidV4(): String {
    return jsUuidV4()
}