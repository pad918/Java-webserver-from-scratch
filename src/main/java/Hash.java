// STOLEN FROM GEEKSFORGEEKS
// This is the harware way of doing hashes in java.
// I could make my own software implementation, but it would be slow and
// Ugly since java does not support unsigned integers

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.text.*;
import java.util.Base64;

public class Hash {

	private static byte[] remove_leading_byte(byte[] arr){
		if(arr.length>20 && (arr[0]==127 || arr[0]==0)){
			byte[] new_arr = new byte[20];
			for(int i=1; i<=20; i++){
				new_arr[i-1] = arr[i];
			}
			arr = new_arr;
		}
		return arr;
	}

	// Slow temporary method
	public static String bigIntToBase64(BigInteger bigInt){
		Base64.Encoder ecoder = Base64.getEncoder();
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] bigIntBytes = remove_leading_byte(bigInt.toByteArray());
		//byte[] b64 = ecoder.encode(bigIntBytes);
		String encoding = ecoder.encodeToString(bigIntBytes);
		return encoding;
		//StringBuilder b64Chars = new StringBuilder("");
		//for (byte b: b64) {
		//	b64Chars.append((char)b);
		//}

		//return b64Chars.toString();
	}

	public static String sha1HashString(String input) {
		try {
			// getInstance() method is called with algorithm SHA-1
			MessageDigest md = MessageDigest.getInstance("SHA-1");

			// digest() method is called
			// to calculate message digest of the input string
			// returned as array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			//BigInteger hex = new BigInteger(1, messageDigest);
			BigInteger  no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = bigIntToBase64(no); //no.toString(16);
			System.out.println("Hex hash = " + no.toString(16));

			// Add preceding 0s to make it 32 bit
			//while (hashtext.length() < 32) {
			//	hashtext = "0" + hashtext;
			//}

			// return the HashText
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}