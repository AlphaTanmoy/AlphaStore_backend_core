package com.alphaStore.Core.contracts

import java.io.*
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec

interface AESUtilContract {
    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun encrypt(algorithm: String?, input: String, key: SecretKey?, iv: IvParameterSpec?): String

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun decrypt(algorithm: String?, cipherText: String?, key: SecretKey?, iv: IvParameterSpec?): String

    @Throws(NoSuchAlgorithmException::class)
    fun generateKey(n: Int): SecretKey

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getKeyFromPassword(password: String, salt: String): SecretKey

    fun generateIv(): IvParameterSpec

    @Throws(
        IOException::class,
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun encryptFile(
        algorithm: String?, key: SecretKey?, iv: IvParameterSpec?,
        inputFile: File?, outputFile: File?
    )

    @Throws(
        IOException::class,
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun decryptFile(
        algorithm: String?, key: SecretKey?, iv: IvParameterSpec?,
        encryptedFile: File?, decryptedFile: File?
    )

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        IOException::class,
        IllegalBlockSizeException::class
    )
    fun encryptObject(
        algorithm: String?, `object`: Serializable?, key: SecretKey?,
        iv: IvParameterSpec?
    ): SealedObject

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        ClassNotFoundException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        IOException::class
    )
    fun decryptObject(
        algorithm: String?, sealedObject: SealedObject, key: SecretKey?,
        iv: IvParameterSpec?
    ): Serializable

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun encryptPasswordBased(plainText: String, key: SecretKey?, iv: IvParameterSpec?): String

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun decryptPasswordBased(cipherText: String?, key: SecretKey?, iv: IvParameterSpec?): String
}