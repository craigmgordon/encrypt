package com.example.config;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EncryptController {

	private static String secretKey = "boooooooooom!!!!";
	private static String salt = "ssshhhhhhhhhhh!!!!";
	
	
	@GetMapping("/encrypt/{raw}")
	public String encrypt(@PathVariable("raw")String raw) {
		System.out.println("@@@@@ in encrypt @@@@@");
		return encrypt(raw, "secret");
	}
	
	
	@GetMapping("/decrypt/{encrypted}")
	public String decrypt(@PathVariable("encrypted")String encrypted) {
		System.out.println("@@@@@ in decrypt @@@@@");
		return decrypt(encrypted, "secret");
	}
	
	private static String encrypt(String strToEncrypt, String secret)
	{
	    try
	    {
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	         
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
	        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	    }
	    catch (Exception e)
	    {
	        System.out.println("Error while encrypting: " + e.toString());
	    }
	    return null;
	}
	
	
	private static String decrypt(String strToDecrypt, String secret) {
	    try
	    {
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	         
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
	        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	    }
	    catch (Exception e) {
	        System.out.println("Error while decrypting: " + e.toString());
	    }
	    return null;
	}
}
