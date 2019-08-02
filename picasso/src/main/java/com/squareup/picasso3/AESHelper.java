package com.squareup.picasso3;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密类
 */
public class AESHelper {

    private AESHelper() {
    }

    public static byte[] encrypt(byte[] messageBytes, byte[] psBytes) throws NullPointerException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (messageBytes == null || psBytes == null) {
            throw new NullPointerException();
        }

        final String ALGORITHM = "AES";
        final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";
        Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(psBytes, ALGORITHM));
        return cipher.doFinal(messageBytes);

    }

    public static byte[] decrypt(byte[] messageBytes, byte[] psBytes) throws NullPointerException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (messageBytes == null || psBytes == null) {
            throw new NullPointerException();
        }
        final String ALGORITHM = "AES";
        final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";
        Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(psBytes, ALGORITHM));
        return cipher.doFinal(messageBytes);
    }
}
