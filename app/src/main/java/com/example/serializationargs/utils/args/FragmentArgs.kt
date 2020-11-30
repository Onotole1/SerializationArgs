package com.example.serializationargs.utils.args

import android.os.Bundle
import com.example.serializationargs.utils.bundle.fromBundle
import com.example.serializationargs.utils.bundle.toBundle
import kotlinx.serialization.KSerializer
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object FragmentArgs {

    fun <T> parcelable(serializer: KSerializer<T>): ReadWriteProperty<Bundle, T?> =
        ParcelableArg(serializer)

    private class ParcelableArg<T>(
        private val serializer: KSerializer<T>
    ) : ReadWriteProperty<Bundle, T?> {

        override fun getValue(thisRef: Bundle, property: KProperty<*>): T? =
            thisRef.getParcelable<Bundle>(property.name)?.fromBundle(serializer)

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: T?) {
            thisRef.putParcelable(property.name, value?.toBundle(serializer))
        }
    }
}
