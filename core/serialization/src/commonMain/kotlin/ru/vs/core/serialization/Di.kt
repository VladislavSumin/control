package ru.vs.core.serialization

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.core.di.Modules
import ru.vs.core.di.i

fun Modules.coreSerialization() = DI.Module("core-serialization") {
//    bindSet<SerializersModule>()

    bindSingleton<JsonFactory> { JsonFactoryImpl() }
    bindSingleton { i<JsonFactory>().createDefault() }
}
