package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.LocalDataBase;

@WebServlet("/DataBaseServlet")
public class DataBaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		Integer sw = Integer.parseInt(request.getParameter("switch"));
		// request to database
		String result = "ok";
		LocalDataBase ldb = new LocalDataBase();
		try {
			ldb.open_connection();
			if (sw == 1) {
				ldb.delete_data_base();
				ldb.create_data_base();
			} else if (sw == 2) {
				ldb.delete_data_base();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "somethin wrong with connect";
		} finally {
			ldb.close_connection();
		}

		// show results to user
		request.setAttribute("exact answer", result);
		request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);

	}

}
