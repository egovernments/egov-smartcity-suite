/*
 * CryptoHelper.java Created on Feb 6, 2006
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.common;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Neetu
 * @version 1.00 
 * @see	    
 * @see	    
 * @since   1.00
 */

public class CryptoHelper
{
	// This is the key string. If this is changed,
	// it will not be possible to decrypt strings
	// with previous keys. So this should NOT BE CHANGED
	// under any circumstances.
	private static final Logger LOGGER = Logger.getLogger(CryptoHelper.class);
	private static final String USERSTR = "-93uscj5q0a";
	private static final String BLOWFISH = "BlowFish";

	public CryptoHelper()
	{
	}

	public String getEncryptedString(String str)
	{
		if(str == null || str.equals("")) {
			return null;
		}
		else {
			try {
				SecretKeySpec keySpec = new SecretKeySpec(new BigInteger(USERSTR, 32).toByteArray(), BLOWFISH);
				SecretKey thisKey = (SecretKey)keySpec;
				Cipher thisCipher = Cipher.getInstance(BLOWFISH);

				// Initialize the cipher for encryption
				thisCipher.init(Cipher.ENCRYPT_MODE, thisKey);

				// cleartext
				byte[] cleartext = str.getBytes();

				// Encrypt the cleartext
				byte[] ciphertext = thisCipher.doFinal(cleartext);

				return new BigInteger(ciphertext).toString(32);
			}
			catch(NoSuchAlgorithmException noAlgoExp) {
				LOGGER.error("Error Occured in getEncryptedString with NoSuchAlgorithmException== "+noAlgoExp.getMessage());
				return null;
			}
			catch(NoSuchPaddingException noPadExp) {
				LOGGER.error("Error Occured in getEncryptedString with NoSuchPaddingException== "+noPadExp.getMessage());
				return null;
			}
			catch(BadPaddingException badPadExp) {
				LOGGER.error("Error Occured in getEncryptedString with BadPaddingException== "+badPadExp.getMessage());
				return null;
			}
			catch(IllegalBlockSizeException illBlkExp) {
				LOGGER.error("Error Occured in getEncryptedString with IllegalBlockSizeException== "+illBlkExp.getMessage());
				return null;
			}
			catch(InvalidKeyException invKeyExp) {
				LOGGER.error("Error Occured in getEncryptedString with InvalidKeyException== "+invKeyExp.getMessage());
				return null;
			}
		}

	}

	public String getDecryptedString(String str)
	{
		if(str == null || str.equals("")) {
			return null;
		}
		else {
			try {
				SecretKeySpec keySpec = new SecretKeySpec(new BigInteger(USERSTR, 32).toByteArray(), BLOWFISH);
				SecretKey thisKey = (SecretKey)keySpec;
				Cipher thisCipher = Cipher.getInstance(BLOWFISH);

				// Initialize the cipher for decryption
				thisCipher.init(Cipher.DECRYPT_MODE, thisKey);

				byte[] encryptedBytes = new BigInteger(str,32).toByteArray();
				byte[] actualBytes = encryptedBytes;

				// Check if the block size is a multiple of 8
				if( (encryptedBytes.length % 8 ) != 0) {
					actualBytes = new byte[encryptedBytes.length + 1];

					if(encryptedBytes[0] < 0) {
						actualBytes[0] = -1;
					}
					else {
						actualBytes[0] = 0;
					}

					System.arraycopy(encryptedBytes, 0, actualBytes, 1, encryptedBytes.length);
				}
				// Decrypt the encrypted string
				byte[] ciphertext = thisCipher.doFinal(actualBytes);
				return new String(ciphertext);
			}
			catch(NoSuchAlgorithmException noAlgoExp) {
				LOGGER.error("Error Occured in getDecryptedString with NoSuchAlgorithmException== "+noAlgoExp.getMessage());
				return null;
			}
			catch(NoSuchPaddingException noPadExp) {
				LOGGER.error("Error Occured in getDecryptedString with NoSuchPaddingException== "+noPadExp.getMessage());
				return null;
			}
			catch(BadPaddingException badPadExp) {
				LOGGER.error("Error Occured in getDecryptedString with BadPaddingException== "+badPadExp.getMessage());
				return null;
			}
			catch(IllegalBlockSizeException illBlkExp) {
				LOGGER.error("Error Occured in getDecryptedString with IllegalBlockSizeException== "+illBlkExp.getMessage());
				return null;
			}
			catch(InvalidKeyException invKeyExp) {
				LOGGER.error("Error Occured in getDecryptedString with InvalidKeyException== "+invKeyExp.getMessage());
				return null;
			}
		}

	}
}