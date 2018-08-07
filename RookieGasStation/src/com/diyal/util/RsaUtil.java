package com.diyal.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RsaUtil {
	// public static byte[] encrpty(byte[] content, String publicKey)
	// throws NoSuchAlgorithmException, InvalidKeySpecException,
	// NoSuchPaddingException, InvalidKeyException,
	// IllegalBlockSizeException, BadPaddingException {
	// LogWriter.print("RsaUtil encrpty() bytesCnt:"
	// + content.length + " key1:" + publicKey);
	// LogWriter.print("RsaUtil a() before RSA str:"
	// + new String(content));
	// BigInteger localBigInteger = new BigInteger("010001", 16);
	// RSAPublicKeySpec localRSAPublicKeySpec = new RSAPublicKeySpec(
	// new BigInteger(publicKey, 16), localBigInteger);
	// PublicKey localPublicKey = KeyFactory.getInstance("RSA")
	// .generatePublic(localRSAPublicKeySpec);
	// Cipher localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	// localCipher.init(1, localPublicKey);
	// byte[] arrayOfByte = localCipher.doFinal(content);
	// LogWriter.print("RsaUtil encrpty() after RSA bytesCnt:"
	// + arrayOfByte.length);
	// return arrayOfByte;
	// }

	private static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzqCzNcB75j9Us7t2dO1hRLXT0"
			+ "\r"
			+ "in+G74znUgpgOt6eDuhX8lc82pMPlp2R2Ka2Fp/byXg5ALyoUsd/sJqYzIfu6Xdz"
			+ "\r"
			+ "JfJbqeMiYUaxpN6YXs64chuCcz7k2fG1FHxLU8ptiFkM0GiAFvknSKCKJsN2GtaL"
			+ "\r" + "y0I+1ZjPivIbIxT/swIDAQAB" + "\r";

	/**
	 * 公钥
	 */
	private static RSAPublicKey publicKey;

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	private static void loadPublicKey(String publicKeyStr) throws Exception {
		try {
			byte[] buffer = Base64Util.base64Decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 加密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	private static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher = Cipher.getInstance("RSA/ECB/NOPADDING");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	public static byte[] encrypt(byte[] plainTextData) throws Exception {
		// load key
		if (publicKey == null) {
			loadPublicKey(DEFAULT_PUBLIC_KEY);
		}

		// encrypt
		return encrypt(publicKey, plainTextData);
	}

	/**
	 * 解密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	private static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/NOPADDING");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	public static byte[] decrypt(byte[] cipherData) throws Exception {
		// load key
		if (publicKey == null) {
			loadPublicKey(DEFAULT_PUBLIC_KEY);
		}

		// encrypt
		return decrypt(publicKey, cipherData);
	}
}
