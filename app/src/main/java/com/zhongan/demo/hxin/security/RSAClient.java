package com.zhongan.demo.hxin.security;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class RSAClient {
	
	private static RSAPublicKey getPublicKey(String public_Modulus, String public_Exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		BigInteger big_N = new BigInteger(public_Modulus, 16);
		BigInteger big_e = new BigInteger(public_Exponent, 16);
		KeySpec keySpec = new RSAPublicKeySpec(big_N, big_e);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}
	
	public static String encoding(String publicModulus, String publicExponent, String encode) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicModulus, publicExponent));
		byte[] bytes = cipher.doFinal(encode.getBytes("UTF-8"));
		String rsaStr = new String(Base64.encode(bytes));
		return Base64.encodeToStr(rsaStr);
	}
	
	public static void main(String[] args) throws Exception {

		String publicModulus= "81559e562c3b4403081c01c16c085458980fc288620329bdd42114341449c4ca29c7e002b1dcf46b5798876108edf6d7b664e247c9fb43a031fb0ca03816087dbf20d08ca00afd0a8c2ec9c8265db5f452a518bca715e75bf32c995c1610e37718ebd407fb39fc725c69e89e7e6193fbfd24183af16050801f8e7b9353e81ec1";
		String publicExponent="10001";
		String encode ="测试代码ssseww2134567*&……%￥#";
		String rs = RSAClient.encoding(publicModulus, publicExponent, encode);
		System.out.println(rs);
	}
}
