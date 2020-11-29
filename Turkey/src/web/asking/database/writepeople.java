package web.asking.database;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.LocalDataBase;

@WebServlet("/writepeople")
public class writepeople extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] namePeoBase = { "name", "surname", "patronymic", "birthday", "city" };

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		Integer id = Integer.parseInt(request.getParameter("id"));

		// requests to data base
		String result = "ok";

		LocalDataBase ldb = new LocalDataBase();
		try {
			ldb.open_connection();
			ldb.extract_people_main(id, request);

		} catch (Exception e) {
			e.printStackTrace();
			result = "somethin wrong with connect";
		} finally {
			ldb.close_connection();
		}

		// show result to user
		request.setAttribute("exact answer", result);
		request.getRequestDispatcher("people_info.jsp").forward(request, response);

	}

}
