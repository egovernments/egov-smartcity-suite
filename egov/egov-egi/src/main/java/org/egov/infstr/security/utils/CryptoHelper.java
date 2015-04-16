/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */


package org.egov.infstr.security.utils;

import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.StringUtils;

public final class CryptoHelper {
	
	private static final Logger LOG = Logger.getLogger(CryptoHelper.class);
	private static final String CRYPTO_ALGO = "PBEWithMD5AndDES";	// Changing this value will disturb existing encrypted data.
	private static final String M_USESTR = "-93uscj5q0aA";	// Changing this value will disturb existing encrypted data.
	private static byte[] SALT = { (byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2 };
	
	/**
	 * Encrypt the given data bytes with the given password
	 **/
	public static final byte[] encrypt(final byte[] inpBytes, final String passowrd) {
		return applyCipher(inpBytes, passowrd,Cipher.ENCRYPT_MODE);
	}

	/**
	 * Decrypt the given encrypted data bytes with the given password
	 **/
	public static final byte[] decrypt(final byte[] inpBytes, final String passowrd) {
		return applyCipher(inpBytes, passowrd,Cipher.DECRYPT_MODE);
	}

	/**
	 * Encrypt the given value
	 */
	public static final String encrypt(final String password) {
		return Base64.encodeBase64String(applyCipher(password,Cipher.ENCRYPT_MODE));
	}
	
	/**
	 * Decrypt the given encrypted value
	 */
	public static final String decrypt(final String password) {
		return new String(applyCipher(password,Cipher.DECRYPT_MODE));
	}

	private static byte [] applyCipher(final String password, final int opMode) {
		if (StringUtils.isBlank(password)) {
			return null;
		} 
		try {
			final SecretKeySpec keySpec = new SecretKeySpec(new BigInteger(M_USESTR, 32).toByteArray(), CRYPTO_ALGO);
			final Cipher cipher = Cipher.getInstance(keySpec.getAlgorithm());
			final PBEParameterSpec paramSpec = new PBEParameterSpec(SALT, 19);
			cipher.init(opMode, keySpec, paramSpec);
			return cipher.doFinal(opMode == Cipher.DECRYPT_MODE ? Base64.decodeBase64(password) : password.getBytes());
		} catch (Exception e) {
			LOG.error(e);
			throw new EGOVRuntimeException("Error occurred while encrypting or decrypting password", e);
		}
	}
	
	private static byte[] applyCipher(final byte[] inpBytes, final String password, final int opmode) {
		if ((inpBytes == null) || (inpBytes.length == 0 ) || StringUtils.isBlank(password)) {
			return null;
		}
		try {
			final PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			final SecretKey key = SecretKeyFactory.getInstance(CRYPTO_ALGO).generateSecret(keySpec);
			final Cipher cipher = Cipher.getInstance(key.getAlgorithm());
			final PBEParameterSpec paramSpec = new PBEParameterSpec(SALT, 19);
			cipher.init(opmode, key, paramSpec);
			return cipher.doFinal(inpBytes);
		} catch (final Exception e) {
			LOG.error(e);
			throw new EGOVRuntimeException("Error occurred while encrypting or decrypting data", e);
		}
	}
}