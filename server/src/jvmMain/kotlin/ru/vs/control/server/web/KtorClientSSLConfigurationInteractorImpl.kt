package ru.vs.control.server.web

import ru.vs.core.ktor_client.AbstractKtorClientSSLConfigurationInteractor
import java.io.File

class KtorClientSSLConfigurationInteractorImpl : AbstractKtorClientSSLConfigurationInteractor() {
    override fun getRootCAFile(): File {
        //TODO написать нормальное решение
        return File("config/ssl/root_test_ca.crt")
    }
}