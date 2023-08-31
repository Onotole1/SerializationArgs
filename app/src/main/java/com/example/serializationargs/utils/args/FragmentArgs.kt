package com.example.serializationargs.utils.args

import android.os.Bundle
import com.example.serializationargs.utils.bundle.fromBundle
import com.example.serializationargs.utils.bundle.toBundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf

object FragmentArgs {

    inline fun <reified T> parcelable(): ReadWriteProperty<Bundle, T?> =
        ParcelableArg(typeOf<T>())

    class ParcelableArg<T>(
        private val type: KType
    ) : ReadWriteProperty<Bundle, T?> {

        override fun getValue(thisRef: Bundle, property: KProperty<*>): T? =
            thisRef.getParcelable<Bundle>(property.name)?.fromBundle(type)

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: T?) {
            thisRef.putParcelable(property.name, value?.toBundle(type))
        }
    }
}
