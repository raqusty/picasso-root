package com.squareup.picasso3;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
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

    public static byte[] encrypt(byte[] messageBytes, byte[] psBytes) throws NullPointerException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        if (messageBytes == null || psBytes == null) {
            throw new NullPointerException();
        }

        final String ALGORITHM = "AES";
        final String ALGORITHM_STR = "AES/ECB/PKCS7Padding";
        Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(psBytes, ALGORITHM));
        return cipher.doFinal(messageBytes);

    }

    public static byte[] decrypt(byte[] messageBytes, byte[] psBytes) throws NullPointerException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        if (messageBytes == null || psBytes == null) {
            throw new NullPointerException();
        }

        final String ALGORITHM = "AES";
        final String ALGORITHM_STR = "AES/ECB/PKCS7Padding";
        Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(psBytes, ALGORITHM));
        return cipher.doFinal(messageBytes);
    }

    /**
     * 初始化 AES Cipher
     *
     * @param key
     * @param cipherMode
     * @return
     */
    @Nullable
    private static Cipher initAESCipher(byte[] key, int cipherMode) {
        Cipher cipher = null;
        try {
            final String ALGORITHM = "AES";
            final String ALGORITHM_STR = "AES/ECB/PKCS7Padding";
            cipher = Cipher.getInstance(ALGORITHM_STR);
            cipher.init(cipherMode, new SecretKeySpec(key, ALGORITHM));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return cipher;
    }

    /**
     * 对文件进行AES加密
     *
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     * @return
     */
    public static boolean encryptFile(byte[] key, String sourceFilePath, String destFilePath) {
        FileInputStream in = null;
        FileOutputStream out = null;
        File destFile = null;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFilePath);
            destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);

                Cipher cipher = initAESCipher(key, Cipher.ENCRYPT_MODE);
                if (cipher == null )
                    return false;
                BufferedInputStream bis = new BufferedInputStream(in);
                byte[] buffer = new byte[1024 * 100];
                int length;
                while ((length = bis.read(buffer)) != -1) {
                    byte[] data = cipher.doFinal(buffer, 0, length);
                    Log.d("demo", "encryptFile doFinal length->" + data.length);
                    out.write(data, 0, data.length);
                    out.flush();
                }
                bis.close();
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return false;
    }

    public static boolean decryptFile(byte[] key, String sourceFilePath, String destFilePath) {
        FileInputStream in = null;
        FileOutputStream out = null;
        File destFile = null;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFilePath);
            destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);

                Cipher cipher = initAESCipher(key, Cipher.DECRYPT_MODE);
                if (cipher == null )
                    return false;
                BufferedOutputStream cipherOutputStream = new BufferedOutputStream(out);
                byte[] buffer = new byte[1024 * 100 + 16];
                int length;
                while ((length = in.read(buffer)) >= 0) {
                    byte[] data = cipher.doFinal(buffer, 0, length);
                    Log.d("demo", "decryptFile doFinal length->" + data.length);
                    cipherOutputStream.write(data, 0, data.length);
                }
                cipherOutputStream.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return false;
    }

    @Nullable
    public static byte[] encryptBytes(byte[] key, byte[] sourceBytes) {
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(sourceBytes);

            Cipher cipher = initAESCipher(key, Cipher.ENCRYPT_MODE);
            if (cipher == null)
                return null;
            BufferedInputStream bis = new BufferedInputStream(in);
            byte[] buffer = new byte[1024 * 100];
            byte[] destBuffer = new byte[sourceBytes.length];
            int nRead;
            while ((nRead = bis.read(buffer)) != -1) {
                System.arraycopy(cipher.doFinal(buffer), 0, destBuffer, 0, nRead);
            }
            bis.close();
            return destBuffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }

    @Nullable
    public static byte[] decryptBytes(byte[] key, byte[] sourceBytes) {
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(sourceBytes);

            Cipher cipher = initAESCipher(key, Cipher.DECRYPT_MODE);
            if (cipher==null)
                return null;
            BufferedInputStream bis = new BufferedInputStream(in);
            byte[] buffer = new byte[1024 * 100 + 16];
            byte[] destBuffer = new byte[sourceBytes.length];
            int nRead;
            while ((nRead = bis.read(buffer)) != -1) {
                System.arraycopy(cipher.doFinal(buffer), 0, destBuffer, 0, nRead);
            }
            bis.close();
            return destBuffer;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in!=null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }
}
