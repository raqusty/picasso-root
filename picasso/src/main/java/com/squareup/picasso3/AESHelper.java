package com.squareup.picasso3;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

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
        return encrypt("AES/ECB/PKCS7Padding", messageBytes, psBytes);
    }

    public static byte[] decrypt(byte[] messageBytes, byte[] psBytes) throws NullPointerException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return decrypt("AES/ECB/PKCS7Padding", messageBytes, psBytes);
    }

    public static byte[] encrypt(String algorithm, byte[] messageBytes, byte[] psBytes) throws NullPointerException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        if (messageBytes == null || psBytes == null) {
            throw new NullPointerException();
        }

        final String ALGORITHM = "AES";
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(psBytes, ALGORITHM));
        return cipher.doFinal(messageBytes);
    }

    public static byte[] decrypt(String algorithm, byte[] messageBytes, byte[] psBytes) throws NullPointerException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        if (messageBytes == null || psBytes == null) {
            throw new NullPointerException();
        }

        final String ALGORITHM = "AES";
        Cipher cipher = Cipher.getInstance(algorithm);
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
        ByteArrayOutputStream out = null;
        try {
            in = new ByteArrayInputStream(sourceBytes);
            out = new ByteArrayOutputStream();

            Cipher cipher = initAESCipher(key, Cipher.ENCRYPT_MODE);
            if (cipher == null)
                return null;
            BufferedInputStream bis = new BufferedInputStream(in);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            byte[] buffer = new byte[1024 * 100];
            int length;
            while ((length = bis.read(buffer)) != -1) {
                byte[] data = cipher.doFinal(buffer, 0, length);
                Log.d("demo", "encryptBytes doFinal length->" + data.length);
                bos.write(data, 0, data.length);
            }
            byte[] data = out.toByteArray();
            bis.close();
            bos.close();
            return data;
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

            try {
                if (out != null) {
                    out.close();
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
        ByteArrayOutputStream out = null;
        try {
            in = new ByteArrayInputStream(sourceBytes);
            out = new ByteArrayOutputStream();

            Cipher cipher = initAESCipher(key, Cipher.DECRYPT_MODE);
            if (cipher == null)
                return null;
            BufferedInputStream bis = new BufferedInputStream(in);
            BufferedOutputStream bos = new BufferedOutputStream(out);

            byte[] buffer = new byte[1024 * 100 + 16];
            int length;
            Log.d("demo", "decryptBytes begin->");
            while ((length = bis.read(buffer)) != -1) {
                byte[] data = cipher.doFinal(buffer, 0, length);
                Log.d("demo", "decryptBytes doFinal length->" + data.length);
                bos.write(data, 0, data.length);
            }
            Log.d("demo", "decryptBytes end->");
            byte[] data = out.toByteArray();
            bis.close();
            bos.close();
            Log.i("linzehao","bos ");
            return data;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Log.i("linzehao","IOException "+e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            Log.i("linzehao","BadPaddingException "+e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            Log.i("linzehao","IllegalBlockSizeException "+e.getMessage());
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
        Log.i("linzehao","null ");
        return null;
    }

    /**
     * 随机生成密码
     *
     * @param length 密码的长度
     * @return 最终生成的密码
     */
    public static String generatePassword(int length) {
        // 最终生成的密码
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 随机生成0或1，用来确定是当前使用数字还是字母 (0则输出数字，1则输出字母)
            int charOrNum = random.nextInt();
            if (charOrNum == 1) {
                // 随机生成0或1，用来判断是大写字母还是小写字母 (0则输出小写字母，1则输出大写字母)
                password.append(random.nextInt(26) + random.nextInt(2) == 1 ? 65 : 97);
            } else {
                // 生成随机数字
                password.append(random.nextInt(10));
            }
        }
        return password.toString();
    }
}
