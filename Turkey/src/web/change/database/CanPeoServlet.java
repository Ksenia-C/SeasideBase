package web.change.database;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.LocalDataBase;

@WebServlet("/CanPeoServlet")
public class CanPeoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_peo = { "StudentName", "StudentSurname", "StudentPatronymic", "StudentBirthday", "StudentCity" };

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		// getting parameters
		String[] aboutPerson = new String[name_peo.length];
		for (int i = 0; i < name_peo.length; i++) {
			aboutPerson[i] = request.getParameter(name_peo[i]);
			if (aboutPerson[i].length() == 0) {
				aboutPerson[i] = null;
			}
		}
		Integer id_homo = 0;
		// requests to data base
		String result = "ok";
		LocalDataBase ldb = new LocalDataBase();
		try {

			ldb.open_connection();
			id_homo = Integer.parseInt(request.getParameter("id"));
			if (request.getParameter("del") != null) {
				ldb.delete_person(id_homo);
			} else {
				ldb.change_person_main(id_homo, aboutPerson);
				ldb.change_person_optional(id_homo, request);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "somethin wrong with connect";
		} finally {
			ldb.close_connection();
		}

		// выход
		request.setAttribute("exact answer", result);
		if (request.getParameter("del") == null) {
			request.getRequestDispatcher("writepeople?id=" + id_homo).forward(request, response);
		} else {
			request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);
		}

	}

}
