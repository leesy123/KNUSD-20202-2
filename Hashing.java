package asdf;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing{
	//해싱 함수. byte[]를 리턴
	
	public static byte[] sha256(String msg)  {
	    MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    md.update(msg.getBytes());
	    
	    return md.digest();
	}
	
	//byte[]를 16진수로 바꿔서 String으로 변환해줌.
	public static String bytesToHex(byte[] bytes) {
	    StringBuilder builder = new StringBuilder();
	    for (byte b: bytes) {
	      builder.append(String.format("%02x", b));
	    }
	    return builder.toString();
	}
}