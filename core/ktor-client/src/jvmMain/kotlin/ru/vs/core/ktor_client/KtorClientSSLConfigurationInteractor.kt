package ru.vs.core.ktor_client

import ru.vs.core.utils.network.KeyStoreUtils
import java.io.File
import java.security.KeyStore
import java.security.cert.Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

interface KtorClientSSLConfigurationInteractor {
    /**
     * return custom SSLConfiguration or null for use system default
     */
    fun getSslConfiguration(): SSLConfiguration?

    data class SSLConfiguration(
        val sslContext: SSLContext,
        val trustManager: X509TrustManager,
    )
}

// TODO так себе реализация, нужно что-то нормальное придумать
abstract class AbstractKtorClientSSLConfigurationInteractor : KtorClientSSLConfigurationInteractor {
    protected abstract fun getRootCAFile(): File

    override fun getSslConfiguration(): KtorClientSSLConfigurationInteractor.SSLConfiguration {
        val certificate = KeyStoreUtils.readCertificate(getRootCAFile())
        val keyStore = createJks(certificate)
        val trustManager = getTrustManager(keyStore)
        val sslContext = getSslContext(trustManager)
        return KtorClientSSLConfigurationInteractor.SSLConfiguration(sslContext, trustManager)
    }

    private fun getSslContext(trustManager: X509TrustManager): SSLContext {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)
        return sslContext
    }

    private fun getTrustManager(keyStore: KeyStore): X509TrustManager {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)
        return trustManagerFactory.trustManagers[0] as X509TrustManager
    }

    private fun createJks(certificate: Certificate): KeyStore {
        val keyStore = KeyStore.getInstance("JKS")!!
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", certificate)
        return keyStore
    }
}