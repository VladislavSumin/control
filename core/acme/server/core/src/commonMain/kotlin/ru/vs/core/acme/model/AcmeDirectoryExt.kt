package ru.vs.core.acme.model

import java.net.URL

fun AcmeDirectory.Companion.createDefault(baseUrl: URL): AcmeDirectory {
    val baseUrlString = baseUrl.toExternalForm()
    return AcmeDirectory(
        newNonce = "$baseUrlString/newNonce",
        newAccount = "$baseUrlString/newAccount",
        newOrder = "$baseUrlString/newOrder",
        newAuthz = "$baseUrlString/newAuthz",
        revokeCert = "$baseUrlString/revokeCert",
        keyChange = "$baseUrlString/keyChange",
    )
}