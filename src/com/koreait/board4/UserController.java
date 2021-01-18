package com.koreait.board4;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreait.board4.common.SecurityUtils;
import com.koreait.board4.common.Utils;
import com.koreait.board4.db.CommonDAO;
import com.koreait.board4.db.SQLInterUpdate;
import com.koreait.board4.db.UserDAO;
import com.koreait.board4.model.UserModel;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class UserController {
//	로그인 페이지 표출(get)
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utils.forwardTemp("로그인", "temp/basic_temp", "user/login", request, response);
	}
	
//	로그인 처리(post)
	public void loginProc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		
		UserModel model = new UserModel();
		model.setUser_id(user_id);

		UserModel loginUser = UserDAO.selUser(model);
		
		if(loginUser == null) { // 아이디 없음
			request.setAttribute("msg",  "아이디가 없습니다. 아이디를 확인해주세요");
			login(request, response);
			return;
		}
		
		String dbPw = loginUser.getUser_pw();
		String dbSalt = loginUser.getSalt();
		String securityPw = SecurityUtils.getSecurePassword(user_pw, dbSalt);
		
		if(!securityPw.equals(dbPw)) {
			request.setAttribute("msg",  "비밀번호가 틀립니다. 비밀번호를 확인해주세요");
//			비밀번호 다름
			login(request, response);
		}else{
//			로그인 성공
			HttpSession session = request.getSession();
			model.setUser_id(null);
			model.setUser_pw(null);
			model.setSalt(null);
			model.setPhone(null);
			model.setR_dt(null);
			model.setProfile_img(null);
			
			session.setAttribute("loginUser", loginUser);
			
			response.sendRedirect("/board/list.korea?typ=1");
		}
	}
	
//	로그아웃
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		
		response.sendRedirect("/user/login.korea");
	}
	
//	회원가입 페이지 표출(get)
	public void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utils.forwardTemp("회원가입", "temp/basic_temp", "user/join", request, response);
	}
	
//	회원가입
	public void joinProc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int result = UserDAO.joinUser(request);

		if(result != 1) {
//			회원가입 오류 발생 시 회원가입 화면으로 이동
			join(request, response);
		}else {
//			회원가입 성공 시 로그인 화면으로 이동
			login(request, response);
		}
	}

//	프로필
	public void profile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserModel p = new UserModel();
		p.setI_user(SecurityUtils.getLoginI_UserPK(request));
		
		request.setAttribute("data", UserDAO.selUser(p));
		request.setAttribute("jsList",  new String[] {"axios.min", "user"});
		
		Utils.forwardTemp("프로필", "temp/basic_temp", "user/profile", request, response);
	}
	
//	프로필 사진 업로드
	public void profileUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		사용자별로 폴더를 만들고 사진을 저장하기위해서 pk값을 얻어온다
		int i_user = SecurityUtils.getLoginI_UserPK(request);
		String savePath = request.getServletContext().getRealPath("res/img/" + i_user);
		System.out.println(savePath);
		int sizeLimit = 104_857_600; // 100MB로 사이즈 제한
		
		/* -> 파일을 삭제할 경우
		File imgFile = new File(savePath + "/파일명.jpg");
		if(imgFile.exists()) {
			imgFile.delete();
		}
		*/
		
		File folder = new File(savePath);
		if(folder.exists()) { //기존 이미지가 있었다면 삭제처리
			File[] folder_list = folder.listFiles(); 
			for(File file : folder_list) {
				if(file.isFile()) {
					file.delete();
				}
			}
			folder.delete();
		}		
		folder.mkdirs();
	
		MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());
		
	//	파일 이름 얻어오기
		Enumeration files = multi.getFileNames();
			
		if(files.hasMoreElements()) {
//			input에 설정해놓은 name
			String ElementName = (String)files.nextElement();
			System.out.println("ElementName : " + ElementName);
				
//			실제로 저장되는 파일 이름 
			String fileName2 = multi.getFilesystemName(ElementName);
			System.out.println("fileName2 : " + fileName2);
				
	//		파일 타입 가져오기
			String fileType = multi.getContentType(ElementName);
			System.out.println("fileType : "+fileType);
				
			String sql = " UPDATE t_user SET"
						 + " profile_img = ?"
						 + " WHERE i_user = ?";
				
			UserDAO.executeUpdate(sql, new SQLInterUpdate() {
					
				@Override
				public void proc(PreparedStatement pstmt) throws SQLException {
					pstmt.setString(1, fileName2);
					pstmt.setInt(2, i_user);
					}
				});
			}
		response.sendRedirect("/user/profile.korea");
	}
	
//	기본이미지로 되돌리기
	public void delProfileImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int i_user = SecurityUtils.getLoginI_UserPK(request);
		String savePath = request.getServletContext().getRealPath("res/img/" + i_user);
		
		File folder = new File(savePath);
		if(folder.exists()) { 
			File[] folder_list = folder.listFiles(); 
			for(File file : folder_list) {
				if(file.isFile()) {
					file.delete();
				}
			}
			folder.delete(); // 하위파일도 전부 삭제 
		}	
		
		String sql = " UPDATE t_user set profile_img = null "
					 + " WHERE i_user = ? ";
		
		UserDAO.executeUpdate(sql, new SQLInterUpdate() {
			
			@Override
			public void proc(PreparedStatement pstmt) throws SQLException {
				pstmt.setInt(1, i_user);
			}
		});
		
		String result = "{\"result\":1}";
		response.setContentType("application/json");
		response.getWriter().print(result);
	}
	
//	비밀번호 변경 페이지 표시
	public void changePw_Page(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utils.forwardTemp("회원가입", "temp/basic_temp", "user/changePw", request, response);
	}
	
//	비밀번호 변경 기능 
	public void changePwProc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String current_pw = request.getParameter("current_pw");
		String user_pw = request.getParameter("user_pw");
		
		UserModel param = new UserModel();
		param.setI_user(SecurityUtils.getLoginI_UserPK(request));
		
		UserModel userInfo = UserDAO.selUser(param);
		String encrypCurrentPw = SecurityUtils.getSecurePassword(current_pw, userInfo.getSalt()); 
		
		if(!userInfo.getUser_pw().equals(encrypCurrentPw)) { //비밀번호가 틀린 경우
			request.setAttribute("msg", "기존 비밀번호를 확인해 주세요.");
			changePw_Page(request, response);
		}
		String encrypUserPw = SecurityUtils.getSecurePassword(user_pw, userInfo.getSalt());
		
		String sql = " UPDATE t_user SET user_pw = ? WHERE i_user = ? ";
		UserDAO.executeUpdate(sql, new SQLInterUpdate() {
			@Override
			public void proc(PreparedStatement ps) throws SQLException {
				ps.setString(1, encrypUserPw);
				ps.setInt(2, SecurityUtils.getLoginI_UserPK(request));
			}
		});
		
		logout(request, response);
	}
}
