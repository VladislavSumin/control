package ru.vs.core.utils.network

import java.io.File
import java.security.KeyFactory
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

object KeyStoreUtils {
    fun readCertificate(file: File): Certificate {
        return file.inputStream().use {
            CertificateFactory.getInstance("X.509").generateCertificate(it)
        }
    }

    fun readKey(file: File): PrivateKey {
        // openssl pkcs8 -topk8 -inform pem -in file.key -outform pem -nocrypt -out file.pem
        //TODO simplify
        val encodedKey = file.readText().replace(Regex("-----[A-Z ]*-----"), "")
            .trim().replace("\n", "")
        val key = Base64.getDecoder().decode(encodedKey)
        val keyFactory = KeyFactory.getInstance("EC")
        val keySpec = PKCS8EncodedKeySpec(key)
        return keyFactory.generatePrivate(keySpec)
    }

    fun readJks(keyFile: File, vararg chainFiles: File): KeyStore {
        val keyStore = KeyStore.getInstance("JKS")!!
        keyStore.load(null, null)

        val chain = chainFiles.map { readCertificate(it) }
        val key = readKey(keyFile)

        keyStore.setKeyEntry("key", key, "".toCharArray(), chain.toTypedArray())

        return keyStore
    }
}