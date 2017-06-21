package _22_searchRest.controller;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import _22_searchRest.model.RestBean;
import _22_searchRest.model.RestDAO;


@WebServlet("/SearchRestServlet")
public class SearchRestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String foodKind = "";
		String storeName = "";
		String foodName = "";
		int count = 0;
		
		RestDAO rd = new RestDAO();
		//取得查詢字串的值
		foodKind = request.getParameter("foodKind");
		storeName = request.getParameter("storeName");
		foodName = request.getParameter("foodName");
		
		//依欄位是否為空值做判斷，會得到不同的count值
		//若非空值時才會丟給RestDAO
		if(foodKind.trim().length() != 0){
			rd.setFoodKind(foodKind);
			count += 1;
		}
		
		if(storeName.trim().length() != 0){
			rd.setStoreName(storeName);
			count += 2;
		}
		
		if(foodName.trim().length() != 0){
			rd.setFoodName(foodName);
			count += 4;
		}
		//把count丟給RestDAO
		rd.setCount(count);
		Collection<RestBean> coll = rd.searchRest();
		if(coll != null){
			request.setAttribute("Restaurants", coll);
			request.getRequestDispatcher("_22_SearchRest/result.jsp").forward(request, response);
			return;
		}
		
	}

}