package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.StreetLightDAO;
import vo.StreetLight;

@WebServlet("/Server")
public class Server extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		
		if("showlist".equals(action)) {
			getList(request,response);
		}else if("update".equals(action)) {
			doUpdate(request,response);
		}else {
			ServletContext ctx = this.getServletContext();
			RequestDispatcher rd = ctx.getRequestDispatcher("/mainpage.jsp");
			rd.forward(request, response);
		}
	}
	private void doUpdate(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		int c = 0;
		try {
			String id = request.getParameter("id");
			String code = request.getParameter("code");
			if(id != null && code != null) {
				StreetLightDAO dao = new StreetLightDAO();
				c = dao.doUpdate(id, Integer.parseInt(code));
			}else {
				c = 300;
				System.out.println("client request parameter error");
			}
			
			PrintWriter out = response.getWriter();
			if(c==200) {
				System.out.println("code :"+c+", success");
				out.print(c);
				//success
			}else {
				System.out.println("code :"+c+", fail");
				out.print(c);
			}
			
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	void getList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 데이터 DAO로부터 받아오기
		try {
			StreetLightDAO dao = new StreetLightDAO();
			ArrayList<StreetLight> list = new ArrayList<StreetLight>();
			int code = dao.doShow(list);
		
			
			if(code == 200) {
				//mobile
				Gson gson = new Gson();
				String listJson=gson.toJson(list);
				request.setAttribute("list", listJson);
				PrintWriter out = response.getWriter();
				System.out.println(listJson);
				out.println(listJson);
				System.out.println(listJson);
			}
			
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		//ServletContext ctx = this.getServletContext();
		//RequestDispatcher rd = ctx.getRequestDispatcher("/result.jsp");
		//rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
