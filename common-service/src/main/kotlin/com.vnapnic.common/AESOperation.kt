package com.vnapnic.common

import java.security.Key
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

class AESOperation private constructor(private val cipher: Cipher, private val key: Key) {
    companion object {
        val instance: AESOperation by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            val seed = "Your Seed"
            val keyGenerator: KeyGenerator = KeyGenerator.getInstance("AES")
            val secureRandom = SecureRandom.getInstance("SHA1PRNG")
            secureRandom.setSeed(seed.toByteArray())
            keyGenerator.init(secureRandom)
            val key: Key = SecretKeySpec(keyGenerator.generateKey().encoded, "AES")
            val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            AESOperation(cipher, key)
        }
    }

    private fun encodeToStr(bytes: ByteArray): String{
        val sb = StringBuffer()
        for (byte in bytes) {
            var hex = Integer.toHexString(byte.toInt() and 0xFF)
            if (hex.length == 1) {
                hex = "0$hex"
            }
            sb.append(hex.toUpperCase())
        }
        return sb.toString()
    }

    private fun decodeToBytes(str: String): ByteArray{
        val result = ByteArray(str.length / 2)
        for (i in 0 until str.length / 2) {
            val high: Int = str.substring(i * 2, i * 2 + 1).toInt(16)
            val low: Int = str.substring(i * 2 + 1, i * 2 + 2).toInt(16)
            result[i] = (high * 16 + low).toByte()
        }
        return result
    }

    fun encrypt(text: String): String {
        this.cipher.init(Cipher.ENCRYPT_MODE, key)
        return encodeToStr(cipher.doFinal(text.toByteArray()))
    }

    fun decrypt(text: String): String {
        this.cipher.init(Cipher.DECRYPT_MODE, key)
        return String(cipher.doFinal(decodeToBytes(text)))
    }
}