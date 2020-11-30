@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.example.serializationargs.utils.bundle

import android.os.Bundle
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule

fun <T> Bundle.fromBundle(deserializer: DeserializationStrategy<T>): T =
    deserializer.deserialize(BundleDecoder(this))

fun <T> T.toBundle(serializer: SerializationStrategy<T>): Bundle =
    Bundle(serializer.descriptor.elementsCount).also {
        serializer.serialize(BundleEncoder(it), this)
    }

private const val BUNDLE_SIZE_KEY = "\$size"

@ExperimentalSerializationApi
private class BundleDecoder(
    private val bundle: Bundle,
    private val elementsCount: Int = -1,
    private val initializer: Boolean = true
) : AbstractDecoder() {

    private var index = -1
    private var key: String = ""

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (++index >= elementsCount) {
            return CompositeDecoder.DECODE_DONE
        }

        key = descriptor.getElementName(index)
        return index
    }

    override fun beginStructure(
        descriptor: SerialDescriptor
    ): CompositeDecoder {

        val bundle = if (initializer) {
            bundle
        } else {
            requireNotNull(bundle.getBundle(key))
        }

        val count = when (descriptor.kind) {
            StructureKind.MAP, StructureKind.LIST -> bundle.getInt(BUNDLE_SIZE_KEY)
            else -> descriptor.elementsCount
        }

        return BundleDecoder(
            bundle = bundle,
            elementsCount = count,
            initializer = false
        )
    }

    override fun decodeBoolean(): Boolean = bundle.getBoolean(key)

    override fun decodeByte(): Byte = bundle.getByte(key)

    override fun decodeChar(): Char = bundle.getChar(key)

    override fun decodeDouble(): Double = bundle.getDouble(key)

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = bundle.getInt(key)

    override fun decodeFloat(): Float = bundle.getFloat(key)

    override fun decodeInt(): Int = bundle.getInt(key)

    override fun decodeLong(): Long = bundle.getLong(key)

    override fun decodeNotNullMark(): Boolean = bundle.containsKey(key)

    override fun decodeNull(): Nothing? = null

    override fun decodeShort(): Short = bundle.getShort(key)

    override fun decodeString(): String = bundle.getString(key).orEmpty()

    override val serializersModule: SerializersModule = SerializersModule {}
}

@ExperimentalSerializationApi
private class BundleEncoder(
    private val bundle: Bundle,
    private val parentBundle: Bundle? = null,
    private val keyInParent: String? = null,
    private val initializer: Boolean = true
) : AbstractEncoder() {

    private var key: String = ""

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        this.key = descriptor.getElementName(index)
        return super.encodeElement(descriptor, index)
    }

    override val serializersModule: SerializersModule = SerializersModule { }

    override fun beginStructure(
        descriptor: SerialDescriptor
    ): CompositeEncoder {
        return if (initializer) {
            BundleEncoder(
                bundle = bundle,
                parentBundle = null,
                keyInParent = key,
                initializer = false
            )
        } else {
            BundleEncoder(
                bundle = Bundle(),
                parentBundle = bundle,
                keyInParent = key,
                initializer = false
            )
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        if (keyInParent.isNullOrBlank()) {
            return
        }

        if (descriptor.kind in arrayOf(StructureKind.LIST, StructureKind.MAP)) {
            val size = key.toIntOrNull()?.inc() ?: 0
            bundle.putInt(BUNDLE_SIZE_KEY, size)
        }

        parentBundle?.putBundle(keyInParent, bundle)
    }

    override fun encodeBoolean(value: Boolean) {
        bundle.putBoolean(key, value)
    }

    override fun encodeByte(value: Byte) {
        bundle.putByte(key, value)
    }

    override fun encodeChar(value: Char) {
        bundle.putChar(key, value)
    }

    override fun encodeDouble(value: Double) {
        bundle.putDouble(key, value)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        bundle.putInt(key, index)
    }

    override fun encodeFloat(value: Float) {
        bundle.putFloat(key, value)
    }

    override fun encodeInt(value: Int) {
        bundle.putInt(key, value)
    }

    override fun encodeLong(value: Long) {
        bundle.putLong(key, value)
    }

    override fun encodeShort(value: Short) {
        bundle.putShort(key, value)
    }

    override fun encodeString(value: String) {
        bundle.putString(key, value)
    }
}
