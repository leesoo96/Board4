package com.koreait.board4.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;

import com.koreait.board4.model.UserModel;

public class SecurityUtils {
	
//	비밀번호 암호화
	public static String getSecurePassword(String password, String salt) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] byteSalt = Base64.decodeBase64(salt);
            md.update(byteSalt);
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }
//  -----------------------------------------------------------------------  
    
//	true -> 로그아웃 상태 / false -> 로그인 상태
	public static boolean isLogout(HttpServletRequest request) {
		return getLoginUser(request) == null;
	}
	
	public static UserModel getLoginUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		return (UserModel)session.getAttribute("loginUser");
	}
//	------------------------------------------------------------------------
	
//	i_user -> 글 작성자
	public static int getLoginI_User(HttpServletRequest request) {
		UserModel m = getLoginUser(request);
		
		return m.getI_user();
	}
}
