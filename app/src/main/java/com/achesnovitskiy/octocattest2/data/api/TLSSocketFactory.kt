package com.achesnovitskiy.octocattest2.data.api

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.*

@Singleton
class TLSSocketFactory @Inject constructor() : SSLSocketFactory() {

    private val delegate: SSLSocketFactory

    private var trustManagers: Array<TrustManager> = arrayOf()

    val trustManager: X509TrustManager?
        get() = trustManagers[0] as X509TrustManager

    init {
        generateTrustManagers()

        val context = SSLContext.getInstance("TLS")

        context.init(null, trustManagers, null)

        delegate = context.socketFactory
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return delegate.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.supportedCipherSuites
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        return enableTLSOnSocket(delegate.createSocket())
    }

    @Throws(IOException::class)
    override fun createSocket(
        s: Socket,
        host: String,
        port: Int,
        autoClose: Boolean
    ): Socket {
        return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose))
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket {
        return enableTLSOnSocket(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        host: String,
        port: Int,
        localHost: InetAddress,
        localPort: Int
    ): Socket {
        return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort))
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket {
        return enableTLSOnSocket(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress,
        port: Int,
        localAddress: InetAddress,
        localPort: Int
    ): Socket {
        return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort))
    }

    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class)
    private fun generateTrustManagers() {
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())

        trustManagerFactory.init(null as KeyStore?)

        val trustManagers = trustManagerFactory.trustManagers

        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            ("Unexpected default trust managers:" + Arrays.toString(trustManagers))
        }

        this.trustManagers = trustManagers
    }

    private fun enableTLSOnSocket(socket: Socket): Socket {
        if (socket is SSLSocket) {
            socket.enabledProtocols = arrayOf(
                "TLSv1.1",
                "TLSv1.2"
            )
        }
        return socket
    }
}