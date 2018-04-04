package com.wuai.company.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
    /**
    *
    * @param plainText
    *            明文
    * @return 32位密�?
    */
   public static String encryption(String plainText) {
       String re_md5 = "";
       try {
           MessageDigest md = MessageDigest.getInstance("MD5");
           try {
			md.update(plainText.getBytes("Utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           byte b[] = md.digest();
           int i;
           StringBuffer buf = new StringBuffer("");
           for (byte aB : b) {
               i = aB;
               if (i < 0)
                   i += 256;
               if (i < 16)
                   buf.append("0");
               buf.append(Integer.toHexString(i));
           }
           re_md5 = buf.toString();
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       return re_md5;
   }
   
   /**
   *
   *            明文
   * @return 32位密�?大写
   */
   public static String encryptionUp(String originstr) {
	   
	   //var hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
       // update MD5 string to lower-case to adapt to UMP test
       // TODO: check lower-case or upperp-case are the MD5 standard
       char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
       try
       {
           byte[] btInput = originstr.getBytes("UTF-8");
           
           MessageDigest mdInst = MessageDigest.getInstance("MD5");
           
           mdInst.update(btInput);
           
           byte[] md = mdInst.digest();
           
           int j = md.length;
           char str[] = new char[j * 2];
           int k = 0;
           for (int i = 0; i < j; i++)
           {
               byte byte0 = md[i];
               str[k++] = hexDigits[byte0 >>> 4 & 0xf];
               str[k++] = hexDigits[byte0 & 0xf];
           }
           return new String(str);
       }
       catch (Exception e)
       {
           e.printStackTrace();
           return null;
       }

	}
	public static void main(String[] args){
	    String a = MD5.encryption("666");
        System.out.println(a);
    }
}
