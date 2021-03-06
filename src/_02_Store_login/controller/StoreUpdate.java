package _02_Store_login.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import _00_init.GlobalService;
import _01_Store_register.model.StoreBean;
import _01_Store_register.model.StoreBeanDAO;

@MultipartConfig(location = "", fileSizeThreshold = 5 * 1024 * 1024, maxFileSize = 1024 * 1024
		* 500, maxRequestSize = 1024 * 1024 * 500 * 5)
@WebServlet("/_02_storeLogin/storeUpdate.do")
public class StoreUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Hello~~");

		request.setCharacterEncoding("UTF-8"); // 文字資料轉內碼
		Map<String, String> errorMsg = new HashMap<String, String>();
		Map<String, String> msgOK = new HashMap<String, String>();
		HttpSession session = request.getSession();
		request.setAttribute("MsgMap", errorMsg); // 顯示錯誤訊息
		session.setAttribute("MsgOK", msgOK); // 顯示正常訊息
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String addr = request.getParameter("address");
		String tel = request.getParameter("tel");
		String email = request.getParameter("eMail");
		String url = request.getParameter("url");
		String[] fileName = new String[3];
		long[] sizeInBytes = new long[3];
		InputStream[] is = new InputStream[3];
		Collection<Part> parts = request.getParts();
		GlobalService.exploreParts(parts, request);
		int i = 0;
		int mode = 0;
		// ----------------------------------------
		StoreBean old_sb = (StoreBean) session.getAttribute("StoreLoginOK");
		if (parts != null) {
			
			for (Part p : parts) {
				if (p.getContentType() != null) {
					String fieldName = p.getName();
					System.out.println("fieldName="+fieldName);
					String value = request.getParameter(fieldName);
					fileName[i] = GlobalService.getFileName(p); // 此為圖片檔的檔名
					fileName[i] = GlobalService.adjustFileName(fileName[i], GlobalService.IMAGE_FILENAME_LENGTH);
					if (fileName[i] != null && fileName[i].trim().length() > 0) {
						sizeInBytes[i] = p.getSize();
						is[i] = p.getInputStream();
						i++;
					} else {
						mode = 1;
					}
				}
			}
		} else {
			errorMsg.put("errTitle", "此表單不是上傳檔案的表單");
		}
		
		// ----------------------------------------
		
		if (!errorMsg.isEmpty()) {
			// 導向原來輸入資料的畫面，這次會顯示錯誤訊息
			RequestDispatcher rd = request.getRequestDispatcher("_storeLoginProfileEditA.jsp");
			rd.forward(request, response);
			return;
		}
		
		StoreBean sb = new StoreBean(username,addr,tel,email,password,url);
		
		StoreBean new_sb = new StoreBean
				(old_sb.getRest_id(), old_sb.getRest_type(), 
				old_sb.getRest_name(), old_sb.getRest_branch(), addr, tel, old_sb.getRest_owner(),
				email, old_sb.getRest_username(), password, url);
		
		StoreBeanDAO dao = new StoreBeanDAO();
		int n = 0;
		if (mode == 0) { // 有修改照片
			n = dao.updateShopData(sb, is[0], is[1], is[2], sizeInBytes[0], sizeInBytes[1], sizeInBytes[2]);
		}else if(mode==1){ // 沒修改照片
			n = dao.updateShopDataNoImg(sb);
		}
		
		if (n == 1) {
			session.setAttribute("StoreLoginOK", new_sb);
			msgOK.put("UpdateOk", "您的更新已成功囉~");
			RequestDispatcher rd = request.getRequestDispatcher("_storeLoginProfileEditA.jsp");
			rd.forward(request, response);

		} else {
			msgOK.put("UpdateFail", "您的更新失敗了...");
			RequestDispatcher rd = request.getRequestDispatcher("_storeLoginProfileEditA.jsp");
			rd.forward(request, response);
		}
		

	}

}
