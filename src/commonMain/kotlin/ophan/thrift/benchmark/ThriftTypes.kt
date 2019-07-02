package ophan.thrift.benchmark

import com.microsoft.thrifty.Adapter
import com.microsoft.thrifty.Struct
import com.microsoft.thrifty.StructBuilder
import com.microsoft.thrifty.TType
import com.microsoft.thrifty.ThriftException
import com.microsoft.thrifty.ThriftField
import com.microsoft.thrifty.protocol.Protocol
import com.microsoft.thrifty.util.ProtocolUtil
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

enum class RequestType(@JvmField val value: Int) {
    /**
     * Fetch the home and all its groups including personalised groups.
     */
    HomeFrontAndGroups(0),

    /**
     * Fetch a front and associated groups.
     */
    FrontAndGroups(1),

    /**
     * Fetch a single group
     */
    Group(2),

    /**
     * Fetch a list.
     */
    List(3),

    /**
     * Fetch a single item.
     */
    Item(4),

    /**
     * Perform a search.
     */
    Search(5);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): RequestType? = when (value) {
            0 -> HomeFrontAndGroups
            1 -> FrontAndGroups
            2 -> Group
            3 -> List
            4 -> Item
            5 -> Search
            else -> null
        }
    }
}

enum class ConnectionType(@JvmField val value: Int) {
    /**
     * The request was performed over Wifi.
     */
    Wifi(0),

    /**
     * The request was performed over a cellular connection.
     */
    WWAN(1);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): ConnectionType? = when (value) {
            0 -> Wifi
            1 -> WWAN
            else -> null
        }
    }
}

enum class BenchmarkType(@JvmField val value: Int) {
    /**
     * Time to display an article from initial tap to DOM being ready.
     */
    TapToArticleDisplay(0),

    /**
     * Time from app launch to home front front being displayed.
     */
    LaunchTime(1);

    companion object {
        @JvmStatic
        fun findByValue(value: Int): BenchmarkType? = when (value) {
            0 -> TapToArticleDisplay
            1 -> LaunchTime
            else -> null
        }
    }
}

data class NetworkOperationData(
    @JvmField @ThriftField(fieldId = 1, isRequired = true) val requestType: RequestType,
    @JvmField @ThriftField(fieldId = 2, isRequired = true) val measuredTimeMs: Long,
    @JvmField @ThriftField(fieldId = 3, isOptional = true) val connectionType: ConnectionType?,
    @JvmField @ThriftField(fieldId = 4, isRequired = true) val success: Boolean = true
) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<NetworkOperationData> {
        private var requestType: RequestType? = null

        private var measuredTimeMs: Long? = null

        private var connectionType: ConnectionType? = null

        private var success: Boolean? = true

        constructor() {
            this.requestType = null
            this.measuredTimeMs = null
            this.connectionType = null
            this.success = true
        }

        constructor(source: NetworkOperationData) {
            this.requestType = source.requestType
            this.measuredTimeMs = source.measuredTimeMs
            this.connectionType = source.connectionType
            this.success = source.success
        }

        fun requestType(requestType: RequestType) = apply { this.requestType = requestType }

        fun measuredTimeMs(measuredTimeMs: Long) = apply { this.measuredTimeMs = measuredTimeMs }

        fun connectionType(connectionType: ConnectionType?) = apply { this.connectionType = connectionType }

        fun success(success: Boolean) = apply { this.success = success }

        override fun build(): NetworkOperationData = NetworkOperationData(requestType = checkNotNull(requestType) { "Required field 'requestType' is missing" },
                measuredTimeMs = checkNotNull(measuredTimeMs) { "Required field 'measuredTimeMs' is missing" },
                connectionType = this.connectionType,
                success = checkNotNull(success) { "Required field 'success' is missing" })
        override fun reset() {
            this.requestType = null
            this.measuredTimeMs = null
            this.connectionType = null
            this.success = true
        }
    }

    private class NetworkOperationDataAdapter : Adapter<NetworkOperationData, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): NetworkOperationData {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val requestType = protocol.readI32().let {
                                RequestType.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type RequestType: $it")
                            }
                            builder.requestType(requestType)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.I64) {
                            val measuredTimeMs = protocol.readI64()
                            builder.measuredTimeMs(measuredTimeMs)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    3 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val connectionType = protocol.readI32().let {
                                ConnectionType.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type ConnectionType: $it")
                            }
                            builder.connectionType(connectionType)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    4 -> {
                        if (fieldMeta.typeId == TType.BOOL) {
                            val success = protocol.readBool()
                            builder.success(success)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    else -> ProtocolUtil.skip(protocol, fieldMeta.typeId)
                }
                protocol.readFieldEnd()
            }
            protocol.readStructEnd()
            return builder.build()
        }

        override fun write(protocol: Protocol, struct: NetworkOperationData) {
            protocol.writeStructBegin("NetworkOperationData")
            protocol.writeFieldBegin("requestType", 1, TType.I32)
            protocol.writeI32(struct.requestType.value)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("measuredTimeMs", 2, TType.I64)
            protocol.writeI64(struct.measuredTimeMs)
            protocol.writeFieldEnd()
            if (struct.connectionType != null) {
                protocol.writeFieldBegin("connectionType", 3, TType.I32)
                protocol.writeI32(struct.connectionType.value)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldBegin("success", 4, TType.BOOL)
            protocol.writeBool(struct.success)
            protocol.writeFieldEnd()
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<NetworkOperationData, Builder> = NetworkOperationDataAdapter()
    }
}

data class BenchmarkData(@JvmField @ThriftField(fieldId = 1, isRequired = true) val type: BenchmarkType, @JvmField @ThriftField(fieldId = 2, isRequired = true) val measuredTimeMs: Long) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<BenchmarkData> {
        private var type: BenchmarkType? = null

        private var measuredTimeMs: Long? = null

        constructor() {
            this.type = null
            this.measuredTimeMs = null
        }

        constructor(source: BenchmarkData) {
            this.type = source.type
            this.measuredTimeMs = source.measuredTimeMs
        }

        fun type(type: BenchmarkType) = apply { this.type = type }

        fun measuredTimeMs(measuredTimeMs: Long) = apply { this.measuredTimeMs = measuredTimeMs }

        override fun build(): BenchmarkData = BenchmarkData(type = checkNotNull(type) { "Required field 'type' is missing" },
                measuredTimeMs = checkNotNull(measuredTimeMs) { "Required field 'measuredTimeMs' is missing" })
        override fun reset() {
            this.type = null
            this.measuredTimeMs = null
        }
    }

    private class BenchmarkDataAdapter : Adapter<BenchmarkData, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): BenchmarkData {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.I32) {
                            val type = protocol.readI32().let {
                                BenchmarkType.findByValue(it) ?: throw ThriftException(ThriftException.Kind.PROTOCOL_ERROR, "Unexpected value for enum type BenchmarkType: $it")
                            }
                            builder.type(type)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.I64) {
                            val measuredTimeMs = protocol.readI64()
                            builder.measuredTimeMs(measuredTimeMs)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    else -> ProtocolUtil.skip(protocol, fieldMeta.typeId)
                }
                protocol.readFieldEnd()
            }
            protocol.readStructEnd()
            return builder.build()
        }

        override fun write(protocol: Protocol, struct: BenchmarkData) {
            protocol.writeStructBegin("BenchmarkData")
            protocol.writeFieldBegin("type", 1, TType.I32)
            protocol.writeI32(struct.type.value)
            protocol.writeFieldEnd()
            protocol.writeFieldBegin("measuredTimeMs", 2, TType.I64)
            protocol.writeI64(struct.measuredTimeMs)
            protocol.writeFieldEnd()
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<BenchmarkData, Builder> = BenchmarkDataAdapter()
    }
}
