package ru.mpei.wifi.models

import android.util.Base64
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class Encoder(publicKeyString: String) {
    private data class KV(val key:String, val value:String)

    private val RSA_ALGORITHM = "RSA"
    private val CIPHER_TYPE_FOR_RSA = "RSA/ECB/PKCS1Padding"
    private val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
    private val cipher = Cipher.getInstance(CIPHER_TYPE_FOR_RSA)
    private val key: PublicKey

    init {
        key = getPublicKeyFromString(publicKeyString)
    }

    private fun getPublicKeyFromString(publicKeyString: String): PublicKey {
        val keyT = publicKeyString
            .replace("-----BEGIN PUBLIC KEY-----", "",false)
            .replace(System.lineSeparator(), "")
            .replace("-----END PUBLIC KEY-----", "",false)

        val keySpec =
            X509EncodedKeySpec(Base64.decode(keyT.toByteArray(),Base64.DEFAULT))
        return keyFactory.generatePublic(keySpec)
    }

    fun getToken(data:  Map<String, java.io.Serializable>): String{
        val dataAr = data.map { KV(it.key,it.value.toString()) }
        dataAr.sortedBy { it.key }

        val builder =  StringBuilder()
        dataAr.forEach { builder.append(it.value) }

        val hash = MessageDigest.getInstance("SHA-256").digest(builder.toString().toByteArray())

        cipher.init(Cipher.ENCRYPT_MODE, key)
        return Base64.encodeToString(cipher.doFinal(hash), Base64.DEFAULT)
    }
}